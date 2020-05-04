package team.fjut.cf.controller.admin;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.component.textsim.TextSimClient;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.po.SpiderLocalizedRecord;
import team.fjut.cf.pojo.transform.TransSpiderLocalizedRecord;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.request.LocalizedProblemVO;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;
import team.fjut.cf.service.ProblemInfoService;
import team.fjut.cf.service.SpiderGetProblemInfoService;
import team.fjut.cf.service.SpiderLocalizedRecordService;

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
    SpiderLocalizedRecordService spiderLocalizedRecordService;

    @Resource
    TextSimClient textSimClient;

    @GetMapping("/list")
    public ResultJson getSpiderProblemList(@RequestParam int page,
                                           @RequestParam int limit) {
        List<SpiderProblemListVO> pages = spiderGetProblemInfoService.pages(page, limit);
        int count = spiderGetProblemInfoService.count();
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, pages, count);
    }

    @GetMapping("/info")
    public ResultJson getSpiderProblemInfo(@RequestParam int id) {
        SpiderGetProblemInfo spiderGetProblemInfo = spiderGetProblemInfoService.selectById(id);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderGetProblemInfo);
    }

    /**
     * @param localProblemId
     * @param isLocalAll
     * @param spiderGetProblemId
     * @param username
     * @return
     * @throws IOException
     */
    @PostMapping("/sim")
    public ResultJson SimProblem(@RequestParam Integer localProblemId,
                                 @RequestParam Boolean isLocalAll,
                                 @RequestParam Integer spiderGetProblemId,
                                 @RequestParam(required = false) String username) throws IOException {
        if (isLocalAll) {
            return new ResultJson(ResultCode.RESOURCE_NOT_EXIST, "暂不允许批量查询！");
        }
        SpiderLocalizedRecord spiderLocalizedRecord = new SpiderLocalizedRecord();
        spiderLocalizedRecord.setLocalProblemId(localProblemId);
        spiderLocalizedRecord.setSpiderGetProblemId(spiderGetProblemId);
        spiderLocalizedRecord.setCreateUser(username);
        // 先插入查重记录
        spiderLocalizedRecordService.insert(spiderLocalizedRecord);
        // 找到对应的题目进行题目格式化
        ProblemInfoToSim problemInfoToSim1 = problemInfoService.selectInfoToSimById(localProblemId);
        ProblemInfoToSim problemInfoToSim2 = spiderGetProblemInfoService.selectToSimById(spiderGetProblemId);
        // 开始本地化
        JSONObject jsonObject = textSimClient.SimTwoProblem(spiderLocalizedRecord.getId().toString(), problemInfoToSim1, problemInfoToSim2);
        spiderLocalizedRecord.setSimRecord(jsonObject.toJSONString());
        spiderLocalizedRecordService.update(spiderLocalizedRecord);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, TransSpiderLocalizedRecord.transformToVo(spiderLocalizedRecord));
    }

    /**
     * @return
     */
    @PostMapping("/sim/report")
    public ResultJson SimProblem(@RequestParam Integer id) {
        List<SpiderLocalizedRecord> spiderLocalizedRecords = spiderLocalizedRecordService.selectByGetProblemId(id);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, TransSpiderLocalizedRecord.transformToVo(spiderLocalizedRecords));
    }

    @PostMapping("/localized")
    public ResultJson localizedProblem(@RequestBody LocalizedProblemVO localizedProblem) {
        boolean b = spiderGetProblemInfoService.localizedProblem(localizedProblem);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, b);
    }
}
