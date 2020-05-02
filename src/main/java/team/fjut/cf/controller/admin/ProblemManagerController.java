package team.fjut.cf.controller.admin;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.po.ProblemSample;
import team.fjut.cf.pojo.po.ProblemView;
import team.fjut.cf.pojo.vo.ProblemListAdminVO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongml [2020/4/17]
 */
@RestController
@RequestMapping("/admin/problems")
@CrossOrigin
public class ProblemManagerController {

    @Resource
    ViewProblemInfoService viewProblemInfoService;

    @Resource
    ProblemInfoService problemInfoService;

    @Resource
    ProblemViewService problemViewService;

    @Resource
    ProblemTagService problemTagService;

    @Resource
    ProblemSampleService problemSampleService;

    @Resource
    ProblemStarService problemStarService;

    /**
     * @param page
     * @param limit
     * @param sort
     * @param title
     * @param difficultLevel
     * @return
     */
    @GetMapping("/list")
    public ResultJson getProblemLimit(@RequestParam("page") Integer page,
                                      @RequestParam("limit") Integer limit,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "difficultLevel", required = false) Integer difficultLevel,
                                      @RequestParam(value = "title", required = false) String title) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (!StringUtils.isEmpty(title)) {
            // 拼接查询字符串
            title = "%" + title + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            title = null;
        }
        List<ProblemListAdminVO> problemList = viewProblemInfoService.selectByCondition(page, limit, sort, title, difficultLevel);
        int total = viewProblemInfoService.countByCondition(title, difficultLevel);
        resultJson.addInfo(problemList);
        resultJson.addInfo(total);
        return resultJson;
    }

    /**
     * @return
     */
    @GetMapping("/all")
    public ResultJson getAllProblem() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<ProblemInfo> problemList = problemInfoService.selectAll();
        resultJson.addInfo(problemList);
        return resultJson;
    }

    /**
     * @param problemId
     * @return
     */
    @GetMapping("/info")
    public ResultJson getProblemInfo(@RequestParam("problemId") Integer problemId) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ProblemInfo problemInfo = problemInfoService.selectProblemInfo(problemId);
        ProblemView problemView = problemViewService.selectView(problemId);
        List<ProblemSample> problemSample = problemSampleService.selectSamples(problemId);
        int totalTag = problemTagService.selectTagRecord(problemId).size();
        int totalStar = problemStarService.selectStar(problemId).size();
        resultJson.addInfo(problemInfo);
        resultJson.addInfo(problemView);
        resultJson.addInfo(problemSample);
        resultJson.addInfo(totalTag);
        resultJson.addInfo(totalStar);
        return resultJson;
    }


    /**
     * @param problemId
     * @param description
     * @param input
     * @param output
     * @param inputCase
     * @param outputCase
     * @return
     */
    @PutMapping("/update")
    public ResultJson updateProblem(@RequestParam("problemId") Integer problemId,
                                    @RequestParam(value = "description", required = false) String description,
                                    @RequestParam(value = "input", required = false) String input,
                                    @RequestParam(value = "output", required = false) String output,
                                    @RequestParam(value = "inputCase", required = false) String inputCase,
                                    @RequestParam(value = "outputCase", required = false) String outputCase){
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ProblemView problemView = new ProblemView();
        problemView.setProblemId(problemId);
        problemView.setDescription(description);
        problemView.setInput(input);
        problemView.setOutput(output);
        int result1 = problemViewService.updateView(problemView);
        if (result1 != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * @param problemId
     * @return
     */
    @DeleteMapping("/delete")
    public ResultJson deleteProblem(@RequestParam("problemId") Integer problemId){
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = problemInfoService.deleteProblem(problemId);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

}
