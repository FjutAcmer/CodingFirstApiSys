package team.fjut.cf.controller.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.component.textsim.TextSimExecClient;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.po.SpiderSimRecord;
import team.fjut.cf.pojo.transform.TransSpiderLocalizedRecord;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.request.LocalizedProblemVO;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;
import team.fjut.cf.service.ProblemInfoService;
import team.fjut.cf.service.SpiderGetProblemInfoService;
import team.fjut.cf.service.SpiderSimRecordService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 爬取题目相关的Controller类
 *
 * @author axiang [2020/4/30]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/spider/problem")
public class SpiderProblemManagerController {
    @Resource
    SpiderGetProblemInfoService spiderGetProblemInfoService;

    @Resource
    ProblemInfoService problemInfoService;

    @Resource
    SpiderSimRecordService spiderSimRecordService;

    @Resource
    TextSimExecClient textSimExecClient;

    /**
     * 爬取暂存题库题目列表
     *
     * @param page
     * @param limit
     * @param spiderJob
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.SPIDER_PROBLEM_MANAGER})
    @GetMapping("/list")
    public ResultJson getSpiderProblemList(@RequestParam int page,
                                           @RequestParam int limit,
                                           @RequestParam(required = false) String spiderJob) {
        if (StringUtils.isEmpty(spiderJob)) {
            spiderJob = null;
        }
        List<SpiderProblemListVO> pages = spiderGetProblemInfoService.pages(page, limit, spiderJob);
        int count = spiderGetProblemInfoService.count();
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, pages, count);
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_PROBLEM_MANAGER})
    @GetMapping("/info")
    public ResultJson getSpiderProblemInfo(@RequestParam int id) {
        SpiderGetProblemInfo spiderGetProblemInfo = spiderGetProblemInfoService.selectById(id);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderGetProblemInfo);
    }

    /**
     * 题目查重
     *
     * @param localProblemId
     * @param isLocalAll
     * @param spiderGetProblemId
     * @param username
     * @return
     * @throws IOException
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_SIM})
    @PostMapping("/sim")
    public ResultJson simTwoProblem(@RequestParam Integer localProblemId,
                                    @RequestParam Boolean isLocalAll,
                                    @RequestParam Integer spiderGetProblemId,
                                    @RequestParam(required = false) String username) throws IOException {
        if (isLocalAll) {
            return new ResultJson(ResultCode.RESOURCE_NOT_EXIST, "暂不允许批量查询！");
        }
        SpiderSimRecord spiderSimRecord = new SpiderSimRecord();
        spiderSimRecord.setLocalProblemId(localProblemId);
        spiderSimRecord.setSpiderGetProblemId(spiderGetProblemId);
        spiderSimRecord.setCreateUser(username);
        // 先插入查重记录
        spiderSimRecordService.insert(spiderSimRecord);
        // 找到对应的题目进行题目格式化
        ProblemInfoToSim problemInfoToSim1 = problemInfoService.selectInfoToSimById(localProblemId);
        ProblemInfoToSim problemInfoToSim2 = spiderGetProblemInfoService.selectToSimById(spiderGetProblemId);
        // 开始本地化
        JSONObject jsonObject = textSimExecClient.SimTwoProblem(spiderSimRecord.getId().toString(), problemInfoToSim1, problemInfoToSim2);
        spiderSimRecord.setSimRecord(jsonObject.toJSONString());
        spiderSimRecordService.update(spiderSimRecord);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, TransSpiderLocalizedRecord.transformToVo(spiderSimRecord));
    }

    /**
     * 查重报告
     *
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_SIM})
    @PostMapping("/sim/report")
    public ResultJson getSimProblemReport(@RequestParam Integer id) {
        List<SpiderSimRecord> spiderSimRecords = spiderSimRecordService.selectByGetProblemId(id);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, TransSpiderLocalizedRecord.transformToVo(spiderSimRecords));
    }

    /**
     * 题目本地化
     *
     * @param localizedProblem
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_LOCALIZED})
    @PostMapping("/localized")
    public ResultJson localizedProblem(@RequestBody LocalizedProblemVO localizedProblem) {
        boolean b = spiderGetProblemInfoService.localizedProblem(localizedProblem);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, b);
    }
}
