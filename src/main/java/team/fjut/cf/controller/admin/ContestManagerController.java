package team.fjut.cf.controller.admin;

import com.alibaba.druid.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.ContestInfoPO;
import team.fjut.cf.pojo.po.ContestProblemPO;
import team.fjut.cf.pojo.vo.ContestInfoVO;
import team.fjut.cf.pojo.po.ContestRegisterUserPO;
import team.fjut.cf.pojo.vo.ContestRegisterUserVO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.response.ContestProblemVO;
import team.fjut.cf.pojo.vo.response.ContestTypeVO;
import team.fjut.cf.service.ContestInfoService;
import team.fjut.cf.service.ContestProblemService;
import team.fjut.cf.service.ContestRegisterService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhongml [2020/4/23]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/contest")
public class ContestManagerController {

    @Resource
    ContestRegisterService contestRegisterService;

    @Resource
    ContestInfoService contestInfoService;

    @Resource
    ContestProblemService contestProblemService;

    @GetMapping("/review/list")
    public ResultJson getContestLimit(@RequestParam("page") Integer page,
                                     @RequestParam("limit") Integer limit,
                                     @RequestParam(value = "sort", required = false) String sort,
                                     @RequestParam(value = "contestKind", required = false) Integer contestKind,
                                     @RequestParam(value = "reviewStatus", required = false) Integer reviewStatus,
                                     @RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "contestId", required = false) Integer contestId) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (StringUtils.isEmpty(username)) {
            username = null;
        } else {
            username = "%" + username + "%";
        }
        List<ContestRegisterUserVO> contestRegisterUserVOS = contestRegisterService.pagesByConditions(page, limit, sort, contestKind, reviewStatus, username, contestId);
        Integer count = contestRegisterService.selectCountByConditions(contestKind, reviewStatus, username);
        resultJson.addInfo(contestRegisterUserVOS);
        resultJson.addInfo(count);
        return resultJson;
    }

    @GetMapping("/countInProgress")
    public ResultJson getContestInProgress() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        Integer count = contestInfoService.countContestInProgress();
        resultJson.addInfo(count);
        return resultJson;
    }

    @GetMapping("/contestTypeCount")
    public ResultJson getContestTypeCount() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<ContestTypeVO> contestTypeCount = contestInfoService.getContestTypeCount();
        resultJson.addInfo(contestTypeCount);
        return resultJson;
    }

    @PutMapping("/review/update")
    public ResultJson updateContest(@RequestParam("id") Integer id,
                                     @RequestParam("reviewStatus") Integer reviewStatus,
                                     @RequestParam("reviewInfo") String reviewInfo) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ContestRegisterUserPO contestRegisterUserPO = new ContestRegisterUserPO();
        contestRegisterUserPO.setId(id);
        contestRegisterUserPO.setReviewStatus(reviewStatus);
        contestRegisterUserPO.setReviewTime(new Date());
        contestRegisterUserPO.setReviewInfo(reviewInfo);
        int result  = contestRegisterService.updateReviewStatus(contestRegisterUserPO);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    @GetMapping("/info")
    public ResultJson getContestInfo(@RequestParam("id") Integer contestId) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ContestInfoVO vo = new ContestInfoVO();
        ContestInfoPO po = contestInfoService.selectByContestId(contestId);
        List<ContestProblemVO> problems = contestProblemService.selectByContestId(contestId);
        List<Integer> problemIds = new ArrayList<>();
        for (ContestProblemVO item: problems) {
            problemIds.add(item.getProblemId());
        }
        vo.setTitle(po.getTitle());
        vo.setId(po.getContestId());
        vo.setContestKind(po.getContestKind());
        vo.setBeginTime(po.getBeginTime());
        vo.setEndTime(po.getEndTime());
        vo.setRegisterBeginTime(po.getRegisterBeginTime());
        vo.setRegisterEndTime(po.getRegisterEndTime());
        vo.setPermissionType(po.getPermissionType());
        vo.setDescription(po.getDescription());
        vo.setPassword(po.getPassword());
        vo.setComputerRating(po.getComputerRating());
        vo.setRankType(po.getRankType());
        vo.setProblemPutTag(po.getProblemPutTag());
        vo.setStatusReadOut(po.getStatusReadOut());
        vo.setShowRegisterList(po.getShowRegisterList());
        vo.setShowBorderList(po.getShowBorderList());
        vo.setShowOtherStatus(po.getShowOtherStatus());
        vo.setCreateUser(po.getCreateUser());
        vo.setProblems(problemIds);
        resultJson.addInfo(vo);
        return resultJson;
    }

    @RequestMapping("/create")
    public ResultJson createContest(@RequestBody ContestInfoVO newContestVO) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<ContestInfoPO> contestInfoPOS = contestInfoService.selectAll();
        Integer contestId = contestInfoPOS.get(0).getContestId() + 1;
        newContestVO.setId(contestId);
        int result1  = contestInfoService.createContest(newContestVO);
        int result2 = contestProblemService.insertContestProblem(newContestVO.getProblems(), contestId);
        if (result1 == 0 || result2 == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    @RequestMapping("/update")
    public ResultJson updateContest(@RequestBody ContestInfoVO newContestVO) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result1  = contestInfoService.updateContest(newContestVO);
        int result2 = contestProblemService.updateContestProblem(newContestVO.getProblems(), newContestVO.getId());
        if (result1 == 0 || result2 == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }
}
