package team.fjut.cf.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.LoginRequired;
import team.fjut.cf.config.interceptor.annotation.PrivateRequired;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.po.ProblemSample;
import team.fjut.cf.pojo.po.ProblemView;
import team.fjut.cf.pojo.po.UserProblemSolved;
import team.fjut.cf.pojo.vo.ProblemListVO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.UserRadarVO;
import team.fjut.cf.service.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2019/10/22]
 */
@RestController
@RequestMapping("/problem")
@CrossOrigin
public class ProblemController {
    @Resource
    UserBaseInfoService userBaseInfoService;

    @Resource
    ProblemService problemService;

    @Resource
    ProblemInfoService problemInfoService;

    @Resource
    ProblemViewService problemViewService;

    @Resource
    ProblemSampleService problemSampleService;

    @Resource
    UserProblemSolvedService userProblemSolvedService;

    @Resource
    ViewProblemInfoService viewProblemInfoService;

    /**
     * @param pageNum
     * @param pageSize
     * @param problemId
     * @param tagId
     * @param title
     * @param username
     * @return
     */
    @GetMapping("/list")
    public ResultJson getProblemLimit(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,
                                      @RequestParam(value = "problemId", required = false) Integer problemId,
                                      @RequestParam(value = "tagId", required = false) Integer tagId,
                                      @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "username", required = false) String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (pageNum == null) {
            pageNum = 0;
        }
        if (pageSize == null || pageSize > 100) {
            pageSize = 50;
        }
        if (!StringUtils.isEmpty(title)) {
            // 拼接查询字符串
            title = "%" + title + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            title = null;
        }
        List<ProblemListVO> problemLists = viewProblemInfoService.pagesByConditions(pageNum, pageSize, problemId, title, tagId, username);
        int count = viewProblemInfoService.countByConditions(problemId, title, tagId);
        resultJson.addInfo(problemLists);
        resultJson.addInfo(count);
        return resultJson;
    }

    @PostMapping("/info")
    public ResultJson getProblemInfo(@RequestParam(value = "username", required = false) String username,
                                     @RequestParam("problemId") Integer problemId) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ProblemInfo problemInfo = problemInfoService.selectProblemInfo(problemId);
        ProblemView problemView = problemViewService.selectView(problemId);
        List<ProblemSample> problemSamples = problemSampleService.selectSamples(problemId);
        resultJson.addInfo(problemInfo);
        resultJson.addInfo(problemView);
        resultJson.addInfo(problemSamples);

        return resultJson;
    }

    @PrivateRequired
    @PostMapping("/userSolved")
    public ResultJson getUserSolved(@RequestParam("username") String username,
                                    @RequestParam("problemId") Integer problemId) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        UserProblemSolved userProblemSolved = new UserProblemSolved();
        if (!StringUtils.isEmpty(username)) {
            userProblemSolved = userProblemSolvedService.selectUserSolved(username, problemId);
        }
        resultJson.addInfo(userProblemSolved);
        return resultJson;
    }

    @LoginRequired
    @GetMapping("/radar")
    public ResultJson getProblemRadar(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<UserRadarVO> userRadarVOS = problemService.selectUserProblemRadarByUsername(username);
        resultJson.addInfo(userRadarVOS);
        return resultJson;
    }

    @PrivateRequired
    @GetMapping("/recommend")
    public ResultJson getRecommendProblem(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        Boolean isExist = userBaseInfoService.isUserExist(username);
        if (!isExist) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "用户不存在");
            return resultJson;
        }
        List<ProblemInfo> problems = problemService.selectRecommendProblemsByUsername(username);
        resultJson.addInfo(problems);
        return resultJson;
    }


}
