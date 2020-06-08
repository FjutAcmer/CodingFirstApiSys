package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.component.email.EmailTool;
import team.fjut.cf.mapper.*;
import team.fjut.cf.pojo.po.UserAuth;
import team.fjut.cf.pojo.po.UserBaseInfo;
import team.fjut.cf.pojo.po.UserCustomInfo;
import team.fjut.cf.pojo.po.UserMessage;
import team.fjut.cf.pojo.vo.UserAcNumBorderVO;
import team.fjut.cf.pojo.vo.UserAcbBorderVO;
import team.fjut.cf.pojo.vo.UserInfoAdminVO;
import team.fjut.cf.pojo.vo.UserRatingBorderVO;
import team.fjut.cf.service.UserBaseInfoService;
import team.fjut.cf.utils.JsonFileTool;
import team.fjut.cf.utils.Sha1Utils;
import team.fjut.cf.utils.UUIDUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2019/10/11]
 */
@Service
public class UserBaseInfoServiceImpl implements UserBaseInfoService {
    @Resource
    UserBaseInfoMapper userBaseInfoMapper;

    @Resource
    UserAuthMapper userAuthMapper;

    @Resource
    UserCustomInfoMapper userCustomInfoMapper;

    @Resource
    UserCheckInMapper userCheckInMapper;

    @Resource
    UserSealMapper userSealMapper;

    @Resource
    UserTitleMapper userTitleMapper;

    @Resource
    UserMessageMapper userMessageMapper;

    @Resource
    EmailTool emailTool;

    @Resource
    JsonFileTool jsonFileTool;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean registerUser(UserBaseInfo userBaseInfo, UserAuth userAuth, UserCustomInfo userCustomInfo) {
        // 插入用户基本信息
        int ans1 = userBaseInfoMapper.insert(userBaseInfo);
        // 插入用户权限信息
        Date currentTime = new Date();
        // 得到随机盐值
        String salt = UUIDUtils.getUUID32();
        // 加盐密码
        String newPassword = salt + userAuth.getPassword();
        // 对加盐密码使用SHA1加密
        String encryptedPwd = Sha1Utils.encode(newPassword);
        userAuth.setUsername(userBaseInfo.getUsername());
        userAuth.setSalt(salt);
        userAuth.setPassword(encryptedPwd);
        userAuth.setAttemptLoginFailCount(0);
        // FIXME: 新注册用户进行锁定，但由于邮箱服务问题暂时跳过该逻辑
        userAuth.setLocked(0);
        userAuth.setUnlockTime(currentTime);
        userAuth.setLastLoginTime(currentTime);
        int ans2 = userAuthMapper.insert(userAuth);
        // 插入头像信息
        userCustomInfo.setUsername(userBaseInfo.getUsername());
        int ans3 = userCustomInfoMapper.insert(userCustomInfo);
        // 发送激活确认邮件
        //SendEmailBO sendEmailBO = new SendEmailBO();
        //sendEmailBO.setTo(userBaseInfoPO.getEmail());
        //sendEmailBO.setSubject("一码当先acm.fjutcoder.com注册激活邮件");
        //sendEmailBO.setText(userBaseInfoPO.getUsername() + "您好：\n"+"欢迎注册一码当先，请点击以下链接激活账号："
        //+"http://xxxxxxxx");
        //emailTool.sendEmail(sendEmailBO);

        // 发送注册成功站内消息
        UserMessage userMessage = new UserMessage();
        userMessage.setToUsername(userBaseInfo.getUsername());
        userMessage.setFromUsername("SYSTEM");
        userMessage.setTitle("亲爱的 " + userBaseInfo.getUsername() + " ，欢迎注册一码当先！");
        userMessage.setText(jsonFileTool.getMsgTemplate("welcome"));
        userMessage.setTime(new Date());
        userMessageMapper.insertSelective(userMessage);
        return ans1 == 1 && ans2 == 1 && ans3 == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean userLogin(String username, String password) {
        // 最大登录尝试次数
        int maxAttemptTimes = 5;
        Example example = new Example(UserAuth.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        // 拿到权限表的内容
        UserAuth userAuth = userAuthMapper.selectOneByExample(example);
        // 取出数据库中盐值
        String salt = userAuth.getSalt();
        String newPwd = salt + password;
        // 对明文密码和盐值做加密
        String encryptPwd = Sha1Utils.encode(newPwd);
        criteria.andEqualTo("password", encryptPwd);
        // 查询用户名密码是否对应上
        int ans = userAuthMapper.selectCountByExample(example);
        // 重置example为只查询username
        example.clear();
        example.createCriteria().andEqualTo("username", username);
        // 登录成功
        if (1 == ans) {
            UserAuth newUserAuth = new UserAuth();
            // 重置尝试失败次数为0
            newUserAuth.setAttemptLoginFailCount(0);
            // 更新最后登录时间
            newUserAuth.setLastLoginTime(new Date());
            userAuthMapper.updateByExampleSelective(newUserAuth, example);
            return true;
        }
        // 登录失败
        else {
            // 拿到该登录失败用户的表记录
            UserAuth temp = userAuthMapper.selectOneByExample(example);
            // 更新登录失败次数+1
            temp.setAttemptLoginFailCount(temp.getAttemptLoginFailCount() + 1);
            // 更新回去
            userAuthMapper.updateByExampleSelective(temp, example);
            // 如果查询上次登录次数即将超限，则还需要设置超时时间
            if (temp.getAttemptLoginFailCount() >= maxAttemptTimes) {
                long timeLaterFiveMin = System.currentTimeMillis() + (60 * 5 * 1000);
                temp.setUnlockTime(new Date(timeLaterFiveMin));
                userAuthMapper.updateByExampleSelective(temp, example);
            }
            return false;
        }
    }


    @Override
    public boolean isUserExist(String username) {
        Example example = new Example(UserAuth.class);
        example.createCriteria().andEqualTo("username", username);
        int num1 = userAuthMapper.selectCountByExample(example);
        Example example1 = new Example(UserBaseInfo.class);
        example1.createCriteria().andEqualTo("username", username);
        int num2 = userBaseInfoMapper.selectCountByExample(example1);
        return (num1 == 1) && (num2 == 1);
    }


    @Override
    public UserBaseInfo selectByUsername(String username) {
        Example example = new Example(UserBaseInfo.class);
        example.createCriteria().andEqualTo("username", username);
        return userBaseInfoMapper.selectOneByExample(example);
    }

    @Override
    public List<UserAcbBorderVO> selectAcbBorder(int pageNum, int pageSize) {
        List<UserAcbBorderVO> userAcbBorderVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(UserBaseInfo.class);
        example.orderBy("acb").desc();
        List<UserBaseInfo> userBaseInfos = userBaseInfoMapper.selectByExample(example);
        for (UserBaseInfo userBase : userBaseInfos) {
            UserAcbBorderVO userAcbBorderVO = new UserAcbBorderVO();
            userAcbBorderVO.setUsername(userBase.getUsername());
            Example example1 = new Example(UserCustomInfo.class);
            example1.createCriteria().andEqualTo("username", userBase.getUsername());
            UserCustomInfo userCustomInfo = userCustomInfoMapper.selectOneByExample(example1);
            String nickname = Objects.isNull(userCustomInfo)
                    ? "【无名氏】"
                    : userCustomInfo.getNickname();
            userAcbBorderVO.setNick(nickname);
            userAcbBorderVO.setAcb(userBase.getAcb());
            userAcbBorderVOS.add(userAcbBorderVO);
        }
        return userAcbBorderVOS;
    }

    @Override
    public List<UserAcNumBorderVO> selectAcNumBorder(int pageNum, int pageSize) {
        List<UserAcNumBorderVO> userAcNumBorderVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(UserBaseInfo.class);
        example.orderBy("acNum").desc();
        List<UserBaseInfo> userBaseInfos = userBaseInfoMapper.selectByExample(example);
        for (UserBaseInfo userBase : userBaseInfos) {
            String username = userBase.getUsername();
            UserAcNumBorderVO userAcNumBorderVO = new UserAcNumBorderVO();
            userAcNumBorderVO.setUsername(username);
            userAcNumBorderVO.setAcNum(userBase.getAcNum());
            Example example1 = new Example(UserCustomInfo.class);
            example1.createCriteria().andEqualTo("username", username);
            UserCustomInfo userCustomInfo = userCustomInfoMapper.selectOneByExample(example1);
            String nickname = Objects.isNull(userCustomInfo)
                    ? "【无名氏】"
                    : userCustomInfo.getNickname();
            userAcNumBorderVO.setNick(nickname);
            userAcNumBorderVOS.add(userAcNumBorderVO);
        }
        return userAcNumBorderVOS;
    }

    @Override
    public List<UserRatingBorderVO> selectRatingBorder(int pageNum, int pageSize) {
        List<UserRatingBorderVO> userRatingBorderVOS = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(UserBaseInfo.class);
        example.orderBy("rating").desc();
        List<UserBaseInfo> userBaseInfos = userBaseInfoMapper.selectByExample(example);
        for (UserBaseInfo userBase : userBaseInfos) {
            String username = userBase.getUsername();
            UserRatingBorderVO userRatingBorderVO = new UserRatingBorderVO();
            userRatingBorderVO.setUsername(username);
            Example example1 = new Example(UserCustomInfo.class);
            example1.createCriteria().andEqualTo("username", username);
            UserCustomInfo userCustomInfo = userCustomInfoMapper.selectOneByExample(example1);
            String nickname = Objects.isNull(userCustomInfo)
                    ? "【无名氏】"
                    : userCustomInfo.getNickname();
            userRatingBorderVO.setNick(nickname);
            userRatingBorderVO.setRating(userBase.getRating());
            userRatingBorderVOS.add(userRatingBorderVO);
        }
        return userRatingBorderVOS;
    }

    // add by zhongml [2020/4/28]
    @Override
    public List<UserInfoAdminVO> pageByCondition(Integer pageNum, Integer pageSize, String sort, String sortItem, String username) {
        PageHelper.startPage(pageNum, pageSize);
        return userBaseInfoMapper.selectByCondition(username, sort, sortItem);
    }

    // add by zhongml [2020/4/28]
    @Override
    public int countByCondition(String username) {
        return userBaseInfoMapper.selectCountByCondition(username);
    }

    @Override
    public int updateACB(String username, Integer ACB) {
        return userBaseInfoMapper.updateACB(username, ACB);
    }

    // add by zhongml [2020/5/12]
    @Override
    public Integer[] getNewRegister(List<String> pastDaysList) {
        Integer[] userActive = new Integer[7];
        // 获取过去7天对应数据
        for (int i = 0; i < 7; i++) {
            userActive[i] = userBaseInfoMapper.selectCountByDate(pastDaysList.get(i));
        }
        return userActive;
    }

}
