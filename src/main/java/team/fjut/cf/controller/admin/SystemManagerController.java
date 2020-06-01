package team.fjut.cf.controller.admin;

import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.UserMessage;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.response.BugReportVO;
import team.fjut.cf.service.BugReportedService;
import team.fjut.cf.service.UserBaseInfoService;
import team.fjut.cf.service.UserMessageService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongml [2020/4/29]
 */
@RestController
@RequestMapping("/admin/system")
@CrossOrigin
public class SystemManagerController {

    @Resource
    UserMessageService userMessageService;

    @Resource
    BugReportedService bugReportedService;

    @Resource
    UserBaseInfoService userBaseInfoService;

    @PostMapping("/message/create")
    @PermissionRequired(permissions = {PermissionType.SYSTEM_ADD_MESSAGE})
    public ResultJson createMessage(@RequestParam("toUsername") String toUsername,
                                @RequestParam("fromUsername") String fromUsername,
                                @RequestParam("title") String title,
                                @RequestParam("text") String text) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = -1;
        if (userBaseInfoService.isUserExist(toUsername) ) {
            UserMessage userMessage = new UserMessage();
            userMessage.setStatus(0);
            userMessage.setFromUsername(fromUsername);
            userMessage.setToUsername(toUsername);
            userMessage.setTitle(title);
            userMessage.setText(text);
            result  = userMessageService.insertMessage(userMessage);
        } else {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "用户不存在，请检查是否输入正确的用户名");
        }
        if (result == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    @GetMapping("/bugReport/list")
    @PermissionRequired(permissions = {PermissionType.SYSTEM_BUG_QUERY})
    public ResultJson bugReportList(@RequestParam("page") Integer pageNum,
                                    @RequestParam("limit") Integer pageSize,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "isFixed", required = false) Integer isFixed) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<BugReportVO> bugReportList = bugReportedService.pageByCondition(pageNum, pageSize, sort, isFixed);
        int total = bugReportedService.countByCondition();
        resultJson.addInfo(bugReportList);
        resultJson.addInfo(total);
        return resultJson;
    }

    @PutMapping("/bugReport/update")
    @PermissionRequired(permissions = {PermissionType.SYSTEM_BUG_QUERY})
    public ResultJson updateBugReport(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = bugReportedService.setIdFixed(id);
        if (result == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }


}
