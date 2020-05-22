package team.fjut.cf.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.LoginRequired;
import team.fjut.cf.pojo.enums.CodeLanguage;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.enums.SubmitResult;
import team.fjut.cf.pojo.po.ContestInfoPO;
import team.fjut.cf.pojo.po.ContestProblemPO;
import team.fjut.cf.pojo.vo.ContestListVO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.response.ContestProblemVO;
import team.fjut.cf.service.ContestInfoService;
import team.fjut.cf.service.ContestProblemService;
import team.fjut.cf.service.JudgeStatusService;
import team.fjut.cf.service.ViewJudgeStatusService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2019/11/18]
 */
@RestController
@CrossOrigin
@RequestMapping("/contest")
public class ContestController {
    @Resource
    ContestInfoService contestInfoService;

    @Resource
    ContestProblemService contestProblemService;

    @Resource
    JudgeStatusService judgeStatusService;

    @Resource
    ViewJudgeStatusService viewJudgeStatusService;

    @GetMapping("/list")
    public ResultJson getContestList(@RequestParam("kind") Integer kind,
                                     @RequestParam("pageNum") Integer pageNum,
                                     @RequestParam("pageSize") Integer pageSize,
                                     @RequestParam(value = "searchName", required = false) String searchName,
                                     @RequestParam(value = "searchPermission", required = false) Integer searchPermission,
                                     @RequestParam(value = "searchStatus", required = false) Integer searchStatus) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (StringUtils.isEmpty(searchName)) {
            searchName = null;
        } else {
            searchName = "%" + searchName + "%";
        }
        List<ContestListVO> contestListVOS = contestInfoService.pagesByConditions(kind, searchName, searchPermission, searchStatus, pageNum, pageSize);
        Integer count = contestInfoService.selectCountByConditions(kind, searchName, searchPermission, searchStatus);
        resultJson.addInfo(contestListVOS);
        resultJson.addInfo(count);
        return resultJson;
    }

    @LoginRequired
    @GetMapping("/info")
    public ResultJson getContestInfo(@RequestParam("contestId") Integer contestId,
                                     @RequestParam(value = "username", required = false) String username) {
        ResultJson resultJson = new ResultJson();
        ContestInfoPO contestInfoPO = contestInfoService.selectByContestId(contestId);
        resultJson.addInfo(contestInfoPO);
        return resultJson;
    }

    @LoginRequired
    @GetMapping("/problem/list")
    public ResultJson getContestProblemList(@RequestParam("contestId") Integer contestId) {
        ResultJson resultJson = new ResultJson();
        List<ContestProblemVO> contestProblemPOS = contestProblemService.selectByContestId(contestId);
        resultJson.addInfo(contestProblemPOS);
        return resultJson;
    }

    @LoginRequired
    @GetMapping("/status/list")
    public ResultJson getContestStatusList(@RequestParam("contestId") Integer contestId,
                                           @RequestParam("pageNum") Integer pageNum,
                                           @RequestParam("pageSize") Integer pageSize,
                                           @RequestParam(value = "nick", required = false) String nick,
                                           @RequestParam(value = "problemId", required = false) Integer problemId,
                                           @RequestParam(value = "result", required = false) String resultStr,
                                           @RequestParam(value = "language", required = false) String languageStr) {
        ResultJson resultJson = new ResultJson();
        if (null == pageNum) {
            pageNum = 1;
        }
        if (null == pageSize) {
            pageSize = 30;
        }
        if (!StringUtils.isEmpty(nick)) {
            nick = "%" + nick + "%";
        } else {
            nick = null;
        }
        Integer result = null;
        Integer language = null;
        if (StringUtils.isEmpty(problemId)) {
            problemId = null;
        }
        if (!StringUtils.isEmpty(resultStr)) {
            result = SubmitResult.getCodeByName(resultStr);
        }
        if (!StringUtils.isEmpty(languageStr)) {
            language = CodeLanguage.getCodeByName(languageStr);
        }
        // FIXME: 这里需要修改
        //List<JudgeStatus> judgeStatuses = judgeStatusService.pagesByConditions(pageNum, pageSize, contestId, nick, problemId, result, language);
        //Integer length = judgeStatusService.selectCountByConditions(contestId, nick, problemId, result, language);
        //resultJsonVO.setStatus(ResultJsonCode.REQUIRED_SUCCESS);
        //resultJsonVO.addInfo(judgeStatuses);
        //resultJsonVO.addInfo(length);
        return resultJson;
    }

}
