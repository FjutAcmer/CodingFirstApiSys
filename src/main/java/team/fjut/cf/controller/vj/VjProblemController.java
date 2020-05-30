package team.fjut.cf.controller.vj;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.component.judge.vjudge.VirtualJudgeHttpClient;
import team.fjut.cf.component.judge.vjudge.pojo.ProblemDescription;
import team.fjut.cf.component.judge.vjudge.pojo.ProblemHtmlParams;
import team.fjut.cf.component.judge.vjudge.pojo.ProblemListParams;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.VjProblemInfo;
import team.fjut.cf.pojo.po.VjUserProblemSolved;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.VjProblemInfoService;
import team.fjut.cf.service.VjUserProblemSolvedService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * VJudge 题目相关API
 *
 * @author axiang [2020/2/10]
 */
@RestController
@CrossOrigin
@RequestMapping("/vj/problem")
public class VjProblemController {
    @Resource
    VirtualJudgeHttpClient virtualJudgeHttpClient;

    @Resource
    VjProblemInfoService vjProblemInfoService;


    @Resource
    VjUserProblemSolvedService vjUserProblemSolvedService;

    @PostMapping("/list")
    public ResultJson getVjProblemList(@RequestParam("pageNum") Integer pageNum,
                                       @RequestParam("pageSize") Integer pageSize,
                                       @RequestParam(value = "OJId", required = false) String OJId,
                                       @RequestParam(value = "category", required = false) String category,
                                       @RequestParam(value = "probNum", required = false) String probNum,
                                       @RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "source", required = false) String source) {
        ResultJson resultJson = new ResultJson();
        ProblemListParams params = new ProblemListParams();
        params.setStart((pageNum - 1) * pageSize);
        params.setLength(pageSize);
        params.setOJId(StringUtils.isEmpty(OJId) ? "All" : OJId);
        params.setCategory(StringUtils.isEmpty(category) ? "all" : category);
        if (!Objects.isNull(probNum)) {
            params.setProbNum(probNum);
        }
        if (!Objects.isNull(title)) {
            params.setTitle(title);
        }
        if (!Objects.isNull(source)) {
            params.setSource(source);
        }
        JSONObject jsonObject = virtualJudgeHttpClient.postProblemList(params);
        if (Objects.isNull(jsonObject)) {
            resultJson.setStatus(ResultCode.RESOURCE_NOT_EXIST);
        } else {
            resultJson.setStatus(ResultCode.REQUIRED_SUCCESS);
            resultJson.addInfo(jsonObject);
            resultJson.addInfo(new Date());
        }
        return resultJson;
    }

    /**
     * 获取题目信息
     * 执行逻辑如下：
     * 1. 先到本地查找是否有缓存的记录
     * 2. 如果没有，则到VJ上获取数据并返回，插入新的数据库记录
     * 3. 如果有，且缓存时间不超过7天，则直接返回数据库中记录
     * 4. 如果有，但缓存时间超过7天，重新到VJ上获取数据并返回，同时更新数据库记录
     *
     * @param oJId
     * @param probNum
     * @param username
     * @return
     */
    @PostMapping("/info")
    public ResultJson getProblemInfo(@RequestParam("OJId") String oJId,
                                     @RequestParam("probNum") String probNum,
                                     @RequestParam(value = "username", required = false) String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ProblemHtmlParams params = new ProblemHtmlParams();
        params.setOJId(oJId);
        params.setProbNum(probNum);
        VjProblemInfo vjProblemInfo = vjProblemInfoService.select(oJId, probNum);
        if (Objects.isNull(vjProblemInfo)) {
            vjProblemInfo = new VjProblemInfo();
            ProblemDescription problemDescription = (ProblemDescription) virtualJudgeHttpClient.getProblemInfo(params);
            resultJson.addInfo(problemDescription);
            String data = JSONObject.toJSONString(problemDescription);
            vjProblemInfo.setOjId(oJId);
            vjProblemInfo.setProbNum(probNum);
            vjProblemInfo.setTime(new Date());
            vjProblemInfo.setData(data);
            vjProblemInfoService.insert(vjProblemInfo);
        } else {
            // 如果超过了7天，重新获取并更新数据
            if (System.currentTimeMillis() - vjProblemInfo.getTime().getTime()
                    >= 1000 * 60 * 60 * 24 * 7) {
                ProblemDescription problemDescription = (ProblemDescription) virtualJudgeHttpClient.getProblemInfo(params);
                resultJson.addInfo(problemDescription);
                String data = JSONObject.toJSONString(problemDescription);
                vjProblemInfo.setData(data);
                vjProblemInfo.setTime(new Date());
                vjProblemInfoService.update(vjProblemInfo);
            } else {
                String data = vjProblemInfo.getData();
                Object obj = JSONObject.parse(data);
                resultJson.addInfo(obj);
            }
        }
        return resultJson;
    }

    @PostMapping("/user_solved")
    public ResultJson getProblemSolved(@RequestParam("OJId") String oJId,
                                       @RequestParam("probNum") String probNum,
                                       @RequestParam("username") String username) {
        VjUserProblemSolved example = new VjUserProblemSolved();
        example.setOjId(oJId);
        example.setProbNum(probNum);
        example.setUsername(username);
        VjUserProblemSolved vjUserProblemSolved = vjUserProblemSolvedService.selectOne(example);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, "", vjUserProblemSolved);
    }

}
