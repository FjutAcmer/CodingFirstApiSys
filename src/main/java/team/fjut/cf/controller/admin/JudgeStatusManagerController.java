package team.fjut.cf.controller.admin;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.JudgeStatus;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.StatusAdminVO;
import team.fjut.cf.pojo.vo.response.AcAndSubmitVO;
import team.fjut.cf.pojo.vo.response.LanguageUsedNumVO;
import team.fjut.cf.service.JudgeStatusService;
import team.fjut.cf.service.ViewJudgeStatusService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author zhongml [2020/4/27]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/judge")
public class JudgeStatusManagerController {

    @Resource
    JudgeStatusService judgeStatusService;

    @Resource
    ViewJudgeStatusService viewJudgeStatusService;

    @GetMapping("/list")
    public ResultJson getStatusList(@RequestParam("page") Integer pageNum,
                                    @RequestParam("limit") Integer pageSize,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "contestId", required = false) Integer contestId,
                                    @RequestParam(value = "username", required = false) String username,
                                    @RequestParam(value = "problemId", required = false) Integer problemId,
                                    @RequestParam(value = "result", required = false) Integer result,
                                    @RequestParam(value = "language", required = false) Integer language) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (StringUtils.isEmpty(username)) {
            username = null;
        } else {
            username = "%" + username + "%";
        }
        List<StatusAdminVO> statusAdminVOS = viewJudgeStatusService.selectByPage(pageNum, pageSize, sort, contestId, username, problemId, result, language);
        int count = viewJudgeStatusService.countByPage(contestId, username, problemId, result, language);
        resultJson.addInfo(statusAdminVOS);
        resultJson.addInfo(count);
        return resultJson;
    }

    @GetMapping("/acAndSubmit")
    public ResultJson getAcAndSubmit() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        // 获取过去7天的日期并加入列表中
        List<String> pastDaysList = new ArrayList<>();
        for (int i = 0; i < 7; i ++) {
            // 依次获取7天内的日期
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - i);
            Date today = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String result = format.format(today);
            pastDaysList.add(result);
        }
        AcAndSubmitVO acAndSubmitVO = viewJudgeStatusService.selectAcAndSubmit(pastDaysList);
        resultJson.addInfo(acAndSubmitVO);
        return resultJson;
    }

    @GetMapping("/languageCount")
    public ResultJson getStatusList() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<LanguageUsedNumVO> languageUsedNumVOS = judgeStatusService.countLanguageUsed();
        resultJson.addInfo(languageUsedNumVOS);
        return resultJson;
    }

    @GetMapping("/info")
    public ResultJson getStatusList(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        JudgeStatus judgeStatus = judgeStatusService.selectJudgeById(id);
        resultJson.addInfo(judgeStatus);
        return resultJson;
    }

}
