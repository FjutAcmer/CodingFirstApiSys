package team.fjut.cf.controller.admin;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.*;
import team.fjut.cf.pojo.vo.*;
import team.fjut.cf.pojo.vo.response.UserActiveVO;
import team.fjut.cf.service.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author axiang [2020/4/28]
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

    @PutMapping("/updateACB")
    public ResultJson resetePsw(@RequestParam("ACB") Integer ACB,
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
        int result2  = userMessageService.insertMessage(userMessage);
        if (result1 == 0 || result2 == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "奖励失败，请重试");
        }
        return resultJson;
    }

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

    @GetMapping("/title/all")
    public ResultJson getAllTitle() {
        ResultJson resultJson = new ResultJson();
        List<UserTitle> userTitles = userTitleService.selectAll();
        resultJson.addInfo(userTitles);
        return resultJson;
    }

    @RequestMapping("/title/create")
    public ResultJson createTitle(@RequestBody UserTitle userTitle) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = userTitleService.createTitle(userTitle);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    @DeleteMapping("/title/delete")
    public ResultJson deleteTitle(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = userTitleService.deleteTitle(id);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    @GetMapping("/checkInRecords")
    public ResultJson getCheckInRecords(@RequestParam("page") Integer pageNum,
                                   @RequestParam("limit") Integer pageSize,
                                   @RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson();
        List<UserCheckIn> checkIns = userCheckInService.pagesByUsername( username, pageNum, pageSize);
        Integer total = userCheckInService.countByUsername(username);
        resultJson.addInfo(checkIns);
        resultJson.addInfo(total);
        return resultJson;
    }

    @PutMapping("/resetPsw")
    public ResultJson resetPsw(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        // 重置默认密码
        int result  = userAuthService.updatePsw(username, "123456");
        if (result == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "重置失败，请重试");
        }
        return resultJson;
    }

    @GetMapping("/active")
    public ResultJson getUserActive() {
        ResultJson resultJson = new ResultJson();
        // 获取过去7天的日期并加入列表中
        List<String> pastDaysList = new ArrayList<>();
        for (int i = 6; i >= 0; i --) {
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




