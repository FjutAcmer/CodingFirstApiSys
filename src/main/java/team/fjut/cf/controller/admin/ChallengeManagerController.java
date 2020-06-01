package team.fjut.cf.controller.admin;

import com.alibaba.druid.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.ChallengeBlockType;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.ChallengeBlockPO;
import team.fjut.cf.pojo.vo.ChallengeBlockAdminVO;
import team.fjut.cf.pojo.vo.ChallengeBlockProblemVO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.ChallengeBlockProblemService;
import team.fjut.cf.service.ChallengeBlockService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongml [2020/4/24]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/challenge")
public class ChallengeManagerController {

    @Resource
    ChallengeBlockService challengeBlockService;

    @Resource
    ChallengeBlockProblemService challengeBlockProblemService;

    /**
     * @param page
     * @param limit
     * @param sort
     * @param name
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.CHALLENGE_MANAGER})
    @GetMapping("/list")
    public ResultJson getChallengeLimit(@RequestParam("page") Integer page,
                                        @RequestParam("limit") Integer limit,
                                        @RequestParam(value = "sort", required = false) String sort,
                                        @RequestParam(value = "name", required = false) String name) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (StringUtils.isEmpty(name)) {
            name = null;
        } else {
            name = "%" + name + "%";
        }
        List<ChallengeBlockAdminVO> challengeBlockAdminVOS = challengeBlockService.selectByCondition(page, limit, sort, name);
        Integer count = challengeBlockService.countByCondition(name);
        resultJson.addInfo(challengeBlockAdminVOS);
        resultJson.addInfo(count);
        return resultJson;
    }

    /**
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.CHALLENGE_MANAGER})
    @GetMapping("/all")
    public ResultJson getAllChallenge() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<ChallengeBlockPO> challengeBlockPOS = challengeBlockService.selectAll();
        resultJson.addInfo(challengeBlockPOS);
        return resultJson;
    }

    /**
     * @param
     * @param challengeBlock
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.CHALLENGE_MANAGER})
    @RequestMapping("/create")
    public ResultJson createChallenge(@RequestBody ChallengeBlockAdminVO challengeBlock) {

        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);

        ChallengeBlockPO challengeBlockPO = new ChallengeBlockPO();
        challengeBlockPO.setId(challengeBlock.getId());
        challengeBlockPO.setName(challengeBlock.getName());
        challengeBlockPO.setBlockType(ChallengeBlockType.getIdByName(challengeBlock.getBlockType()));
        challengeBlockPO.setDescription(challengeBlock.getDescription());
        challengeBlockService.createChallenge(challengeBlockPO);
        challengeBlockProblemService.insertProblems(challengeBlock.getId(), challengeBlock.getChallengeProblems());
        challengeBlockService.insertConditionBlocks(challengeBlock.getId(), challengeBlock.getPreconditionBlocks());
        // 暂未判定每条是否都插入成功
        return resultJson;
    }

    /**
     * @param
     * @param challengeBlock
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.CHALLENGE_MANAGER})
    @RequestMapping("/update")
    public ResultJson updateChallenge(@RequestBody ChallengeBlockAdminVO challengeBlock) {

        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);

        ChallengeBlockPO challengeBlockPO = new ChallengeBlockPO();
        challengeBlockPO.setId(challengeBlock.getId());
        challengeBlockPO.setName(challengeBlock.getName());
        challengeBlockPO.setBlockType(ChallengeBlockType.getIdByName(challengeBlock.getBlockType()));
        challengeBlockPO.setDescription(challengeBlock.getDescription());

        challengeBlockService.updateChallenge(challengeBlockPO);
        challengeBlockProblemService.deleteProblems(challengeBlock.getId());
        challengeBlockService.deleteConditions(challengeBlock.getId());
        challengeBlockProblemService.insertProblems(challengeBlock.getId(), challengeBlock.getChallengeProblems());
        challengeBlockService.insertConditionBlocks(challengeBlock.getId(), challengeBlock.getPreconditionBlocks());
        // 暂未判定每条是否都插入成功
        return resultJson;
    }

    /**
     * @param
     * @param blockId
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.CHALLENGE_MANAGER})
    @DeleteMapping("/delete")
    public ResultJson deleteChallenge(@RequestParam("id") Integer blockId) {

        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        challengeBlockService.deleteChallenge(blockId);
        challengeBlockProblemService.deleteProblems(blockId);
        challengeBlockService.deleteConditions(blockId);
        // 暂未判定每条是否都删除成功
        return resultJson;
    }

    /**
     * @param page
     * @param limit
     * @param sort
     * @param blockId
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.CHALLENGE_MANAGER})
    @GetMapping("/problem/list")
    public ResultJson getChallengeProblemLimit(@RequestParam("page") Integer page,
                                               @RequestParam("limit") Integer limit,
                                               @RequestParam(value = "sort", required = false) String sort,
                                               @RequestParam("blockId") Integer blockId) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<ChallengeBlockProblemVO> challengeBlockVOS = challengeBlockProblemService.selectProblemByBlockId(page, limit, sort, blockId);
        Integer count = challengeBlockProblemService.countProblemByBlockId(blockId);
        resultJson.addInfo(challengeBlockVOS);
        resultJson.addInfo(count);
        return resultJson;
    }
}
