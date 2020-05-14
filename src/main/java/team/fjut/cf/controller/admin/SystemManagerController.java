package team.fjut.cf.controller.admin;

import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.BugReport;
import team.fjut.cf.pojo.po.UserMessage;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.BugReportedService;
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

    @PutMapping("message/create")
    public ResultJson createMessage(@RequestParam("toUsername") String toUsername,
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
        int result  = userMessageService.insertMessage(userMessage);
        if (result == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    @GetMapping("log/list")
    public ResultJson createMessage(@RequestParam("page") Integer pageNum,
                                    @RequestParam("limit") Integer pageSize,
                                    @RequestParam("sort") String sort) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<BugReport> bugReportList = bugReportedService.pageByCondition(pageNum, pageSize, sort);
        int total = bugReportedService.countByCondition();
        resultJson.addInfo(bugReportList);
        resultJson.addInfo(total);
        return resultJson;
    }


}
