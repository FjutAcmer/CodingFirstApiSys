package team.fjut.cf.controller.admin;


import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.UserCheckIn;
import team.fjut.cf.pojo.po.UserMessage;
import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.UserInfoAdminVO;
import team.fjut.cf.service.*;

import javax.annotation.Resource;
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
    UserCustomInfoService userCustomInfoService;

    @Resource
    UserMessageService userMessageService;

    @Resource
    UserCheckInService userCheckInService;

    @GetMapping("/list")
    public ResultJson getUserList(@RequestParam("page") Integer pageNum,
                                  @RequestParam("limit") Integer pageSize,
                                  @RequestParam(name = "username", required = false) String username) {
        ResultJson resultJson = new ResultJson();
        if (!StringUtils.isEmpty(username)) {
            // 拼接查询字符串
            username = "%" + username + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            username = null;
        }
        List<UserInfoAdminVO> userInfoAdminVOS = userBaseInfoService.pageByCondition(pageNum, pageSize, username);
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
                                  @RequestParam(name = "name", required = false) String name) {
        ResultJson resultJson = new ResultJson();
        if (!StringUtils.isEmpty(name)) {
            // 拼接查询字符串
            name = "%" + name + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            name = null;
        }
        List<UserTitle> userTitles = userTitleService.pageByCondition(pageNum, pageSize, name);
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

    @RequestMapping("/title/add")
    public ResultJson createTitle(@RequestBody UserTitle userTitle) {
        ResultJson resultJson = new ResultJson();
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

}




