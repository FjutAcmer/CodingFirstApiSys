package team.fjut.cf.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.component.token.TokenModel;
import team.fjut.cf.component.token.jwt.JwtTokenManager;
import team.fjut.cf.config.interceptor.annotation.CaptchaRequired;
import team.fjut.cf.config.interceptor.annotation.LoginRequired;
import team.fjut.cf.config.interceptor.annotation.PrivateRequired;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.ChallengeUserOpenBlockPO;
import team.fjut.cf.pojo.po.UserAuth;
import team.fjut.cf.pojo.po.UserBaseInfo;
import team.fjut.cf.pojo.po.UserCustomInfo;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.UserCustomInfoVO;
import team.fjut.cf.service.*;
import team.fjut.cf.utils.IpUtils;
import team.fjut.cf.utils.JsonFileTool;
import team.fjut.cf.utils.UUIDUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author axiang [2019/10/11]
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Resource
    UserBaseInfoService userBaseInfoService;

    @Resource
    UserCustomInfoService userCustomInfoService;

    @Resource
    UserAuthService userAuthService;

    @Resource
    JudgeStatusService judgeStatusService;

    @Resource
    BorderHonorRankService borderHonorRankService;

    @Resource
    ChallengeBlockService challengeBlockService;

    @Resource
    JwtTokenManager jwtTokenManager;

    @Resource
    JsonFileTool jsonFileTool;

    /**
     * 获取游客token
     *
     * @param request
     * @return
     */
    @PostMapping("/guest/token")
    public ResultJson requireGuestToken(HttpServletRequest request) {
        TokenModel tokenModel = new TokenModel();
        tokenModel.setIp(IpUtils.getClientIpAddress(request));
        tokenModel.setUsername(UUIDUtils.getUUID32());
        tokenModel.setRole("Guest");
        String token = jwtTokenManager.createToken(tokenModel);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, "", token);
    }

    /**
     * 用户登录
     *
     * @param request
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResultJson userLogin(HttpServletRequest request,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) {
        ResultJson resultJson = new ResultJson();
        Date currentDate = new Date();
        // 校验用户名和密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "登录失败！用户名或者密码为空！");
            return resultJson;
        }
        // 校验用户名是否存在
        if (!userBaseInfoService.isUserExist(username)) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "登录失败！用户不存在！");
            return resultJson;
        }
        // 查询账号是否激活
        if (userAuthService.isUserLocked(username)) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "登录失败！您的账号还未激活，请联系管理员！");
            return resultJson;
        }
        // 查询登录权限的解锁时间
        Date unlockTime = userAuthService.selectUnlockTime(username);
        // 如果当前时间小于解锁时间，则表示账号还在锁定期，无法登录
        if (0 > currentDate.compareTo(unlockTime)) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "登录失败！您的账号已暂时被锁定，请稍后登录。如有疑问，请联系管理员");
            return resultJson;
        }
        if (userBaseInfoService.userLogin(username, password)) {
            resultJson.setStatus(ResultCode.REQUIRED_SUCCESS, "登录成功！");
            UserCustomInfoVO userCustomInfoVO = userCustomInfoService.select(username);
            TokenModel tokenModel = new TokenModel();
            tokenModel.setIp(IpUtils.getClientIpAddress(request));
            tokenModel.setUsername(username);
            tokenModel.setRole("User");
            String token = jwtTokenManager.createToken(tokenModel);
            resultJson.addInfo(username);
            resultJson.addInfo(token);
            resultJson.addInfo(userCustomInfoVO);
        } else {
            Integer attemptCount = userAuthService.selectAttemptNumber(username);
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "登录失败！账号或密码不正确！您还有 " + (Math.max(5 - attemptCount, 0)) + "次机会");
        }
        return resultJson;
    }


    @CaptchaRequired
    @PostMapping("/register")
    public ResultJson userRegister(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("nickname") String nickname,
                                   @RequestParam("gender") Integer gender,
                                   @RequestParam("email") String email,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("motto") String motto,
                                   @RequestParam("avatarUrl") String avatarUrl) {
        ResultJson resultJson = new ResultJson();
        boolean isExist = userBaseInfoService.isUserExist(username);
        if (isExist) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "注册的用户已存在！");
            return resultJson;
        }
        boolean isRestrict = jsonFileTool.isUsernameRestrict(username);
        if (isRestrict) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "用户名中含有关键字，请选择新的用户名后重试");
            return resultJson;
        }
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUsername(username);
        userBaseInfo.setGender(gender);
        userBaseInfo.setEmail(email);
        userBaseInfo.setPhone(phone);
        userBaseInfo.setMotto(motto);
        userBaseInfo.setRegisterTime(new Date());
        userBaseInfo.setRating(0);
        userBaseInfo.setAcb(0);
        UserAuth userAuth = new UserAuth();
        userAuth.setPassword(password);
        UserCustomInfo userCustomInfo = new UserCustomInfo();
        userCustomInfo.setAvatarUrl(avatarUrl);
        userCustomInfo.setNickname(nickname);
        boolean ans = userBaseInfoService.registerUser(userBaseInfo, userAuth, userCustomInfo);
        if (ans) {
            resultJson.setStatus(ResultCode.REQUIRED_SUCCESS, "用户注册成功！");
            // 插入挑战模式解锁记录
            ChallengeUserOpenBlockPO challengeUserOpenBlockPO = new ChallengeUserOpenBlockPO();
            challengeUserOpenBlockPO.setUsername(username);
            challengeUserOpenBlockPO.setBlockId(1);
            challengeUserOpenBlockPO.setUnlockTime(new Date());
            challengeBlockService.unlockBlock(challengeUserOpenBlockPO);
        } else {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "用户注册失败！");
        }
        return resultJson;
    }

    @PrivateRequired
    @PostMapping("/logout")
    public ResultJson userLogOut(String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        jwtTokenManager.deleteToken(username);
        return resultJson;
    }

    @LoginRequired
    @PostMapping("/info")
    public ResultJson getUserInfo(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson();
        UserBaseInfo userBaseInfoVO = userBaseInfoService.selectByUsername(username);
        UserCustomInfoVO userCustomInfoVO = userCustomInfoService.select(username);
        Integer totalSubmit = judgeStatusService.selectCountByUsername(username);
        resultJson.addInfo(userBaseInfoVO);
        resultJson.addInfo(userCustomInfoVO);
        resultJson.addInfo(totalSubmit);
        return resultJson;
    }


    @PostMapping("/info/custom")
    public ResultJson getUserCustomerInfo(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson();
        UserCustomInfoVO userCustomInfoVO = userCustomInfoService.select(username);
        resultJson.addInfo(userCustomInfoVO);
        return resultJson;
    }

    @LoginRequired
    @PostMapping("/award")
    public ResultJson getUserAwardList(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson();
        List<String> awardStr = borderHonorRankService.selectByUsername(username);
        resultJson.addInfo(awardStr);
        return resultJson;
    }

}




