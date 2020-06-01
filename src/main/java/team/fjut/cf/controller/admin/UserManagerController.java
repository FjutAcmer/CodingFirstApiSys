package team.fjut.cf.controller.admin;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.UserCheckIn;
import team.fjut.cf.pojo.po.UserMessage;
import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.UserInfoAdminVO;
import team.fjut.cf.pojo.vo.response.UserActiveVO;
import team.fjut.cf.service.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhongml [2020/4/28]
 */
@RestController
@RequestMapping("/admin/user")
@CrossOrigin
public class UserManagerController {
    @Resource
    UserBaseInfoService userBaseInfoService;

    @Resource
    UserTitleService userTitleService;

    @Resource
    UserActiveService userActiveService;

    @Resource
    UserMessageService userMessageService;

    @Resource
    UserCheckInService userCheckInService;

    @Resource
    UserAuthService userAuthService;

    /**
     * 管理员用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param sortItem
     * @param username
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_LIST_QUERY})
    @GetMapping("/list")
    public ResultJson getUserList(@RequestParam("page") Integer pageNum,
                                  @RequestParam("limit") Integer pageSize,
                                  @RequestParam(name = "sort", required = false) String sort,
                                  @RequestParam(name = "sortItem", required = false) String sortItem,
                                  @RequestParam(name = "username", required = false) String username) {
        ResultJson resultJson = new ResultJson();
        if (!StringUtils.isEmpty(username)) {
            // 拼接查询字符串
            username = "%" + username + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            username = null;
        }
        List<UserInfoAdminVO> userInfoAdminVOS = userBaseInfoService.pageByCondition(pageNum, pageSize, sort, sortItem, username);
        Integer total = userBaseInfoService.countByCondition(username);
        resultJson.addInfo(userInfoAdminVOS);
        resultJson.addInfo(total);
        return resultJson;
    }

    /**
     * 奖励ACB
     *
     * @param ACB
     * @param toUsername
     * @param fromUsername
     * @param title
     * @param text
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_AWARD_ACB})
    @PutMapping("/updateACB")
    public ResultJson updateACB(@RequestParam("ACB") Integer ACB,
                                @RequestParam("toUsername") String toUsername,
                                @RequestParam("fromUsername") String fromUsername,
                                @RequestParam("title") String title,
                                @RequestParam("text") String text) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        UserMessage userMessage = new UserMessage();
        userMessage.setStatus(0);
        userMessage.setFromUsername(fromUsername);
        userMessage.setToUsername(toUsername);
        userMessage.setTitle(title);
        userMessage.setText(text);
        int result1 = userBaseInfoService.updateACB(toUsername, ACB);
        int result2 = userMessageService.insertMessage(userMessage);
        if (result1 == 0 || result2 == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "奖励失败，请重试");
        }
        return resultJson;
    }

    /**
     * 重置密码
     *
     * @param username
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_RESET_PWD})
    @PutMapping("/resetPsw")
    public ResultJson resetPassword(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        // 重置默认密码
        int result = userAuthService.updatePassword(username, "123456");
        if (result == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "重置失败，请重试");
        }
        return resultJson;
    }

    /**
     * 查询称号列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param name
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_TITLE_MANAGER})
    @GetMapping("/title/list")
    public ResultJson getUserTitle(@RequestParam("page") Integer pageNum,
                                   @RequestParam("limit") Integer pageSize,
                                   @RequestParam(name = "sort", required = false) String sort,
                                   @RequestParam(name = "name", required = false) String name) {
        ResultJson resultJson = new ResultJson();
        if (!StringUtils.isEmpty(name)) {
            // 拼接查询字符串
            name = "%" + name + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            name = null;
        }
        List<UserTitle> userTitles = userTitleService.pageByCondition(pageNum, pageSize, sort, name);
        Integer total = userTitleService.countByCondition(name);
        resultJson.addInfo(userTitles);
        resultJson.addInfo(total);
        return resultJson;
    }

    /**
     * 新建称号
     *
     * @param userTitle
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_TITLE_MANAGER})
    @RequestMapping("/title/create")
    public ResultJson createTitle(@RequestBody UserTitle userTitle) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = userTitleService.createTitle(userTitle);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * 删除称号
     *
     * @param id
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_TITLE_MANAGER})
    @DeleteMapping("/title/delete")
    public ResultJson deleteTitle(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = userTitleService.deleteTitle(id);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * 签到记录查询
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.USER_CHECKIN_LIST_QUERY})
    @GetMapping("/checkInRecords")
    public ResultJson getCheckInRecords(@RequestParam("page") Integer pageNum,
                                        @RequestParam("limit") Integer pageSize,
                                        @RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson();
        List<UserCheckIn> checkIns = userCheckInService.pagesByUsername(username, pageNum, pageSize);
        Integer total = userCheckInService.countByUsername(username);
        resultJson.addInfo(checkIns);
        resultJson.addInfo(total);
        return resultJson;
    }

    /**
     * @return
     */
    @PermissionRequired(permissions = {})
    @GetMapping("/active")
    public ResultJson getUserActive() {
        ResultJson resultJson = new ResultJson();
        // 获取过去7天的日期并加入列表中
        List<String> pastDaysList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            // 依次获取7天内的日期
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - i);
            Date today = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String result = format.format(today);
            pastDaysList.add(result);
        }
        UserActiveVO userActiveVO = new UserActiveVO();
        Integer[] userActive = userActiveService.getUserActive(pastDaysList);
        Integer[] newRegister = userBaseInfoService.getNewRegister(pastDaysList);
        userActiveVO.setActive(userActive);
        userActiveVO.setNewRegister(newRegister);
        resultJson.addInfo(userActiveVO);
        return resultJson;
    }

}




