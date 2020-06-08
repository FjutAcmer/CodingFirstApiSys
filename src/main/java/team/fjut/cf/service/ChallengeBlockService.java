package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ChallengeBlockConditionPO;
import team.fjut.cf.pojo.po.ChallengeBlockPO;
import team.fjut.cf.pojo.po.ChallengeUserOpenBlockPO;
import team.fjut.cf.pojo.vo.ChallengeBlockAdminVO;
import team.fjut.cf.pojo.vo.ChallengeBlockVO;
import team.fjut.cf.pojo.vo.UserChallengeBlockVO;
import team.fjut.cf.pojo.vo.response.ChallengeBlockConditionVO;

import java.util.List;

/**
 * @author axiang [2019/11/11]
 */
public interface ChallengeBlockService {

    /**
     * 插入一条解锁记录并发送解锁消息
     *
     * @param challengeUserOpenBlockPO
     * @return
     */
    Integer unlockBlock(ChallengeUserOpenBlockPO challengeUserOpenBlockPO);

    /**
     * 根据用户名查询挑战模式模块
     *
     * @param username
     * @return
     */
    List<UserChallengeBlockVO> selectByUsername(String username);

    /**
     * 根据模块ID查询模块详情
     *
     * @param blockId
     * @param username
     * @return
     */
    ChallengeBlockVO selectByBlockIdAndUsername(Integer blockId, String username);

    /**
     * 查询模块之间的关系
     *
     * @return
     */
    List<ChallengeBlockConditionPO> selectConditions();

    /**
     * 查询模块解锁条件
     *
     * @param blockId
     * @return
     */
    List<ChallengeBlockConditionVO> selectConditionByBlockId(Integer blockId);


    /**
     * 更新挑战模块开放模块
     * 逻辑如下：
     * 已知使用的前置条件：一道题目完成了判题，返回了AC结果
     * 先获取该题隶属挑战模块，如果不属于任何模块则直接退出，如果属于进入下一步骤
     * 获取题目隶属挑战模块的后置模块（所有可能会解锁的待解锁模块）
     * 依次对这些可能会解锁的待解锁模块计算解锁条件，条件全部满足则解锁
     *
     * @param username
     * @param problemId
     * @return
     */
    void updateOpenBlock(String username, Integer problemId);

    /**
     * 条件查询挑战模块
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param name
     * @return
     * @author zhongml [2020/4/24]
     */
    List<ChallengeBlockAdminVO> selectByCondition(Integer pageNum, Integer pageSize, String sort, String name);

    /**
     * 条件查询挑战模块数量
     *
     * @param name
     * @return
     * @author zhongml [2020/4/24]
     */
    int countByCondition(String name);

    /**
     * 查询所有模块
     *
     * @return
     * @author zhongml [2020/4/24]
     */
    List<ChallengeBlockPO> selectAll();

    /**
     * 新增一个模块
     *
     * @param challengeBlockPO
     * @return
     * @author zhongml [2020/4/24]
     */
    int createChallenge(ChallengeBlockPO challengeBlockPO);

    /**
     * 修改模块信息
     *
     * @param challengeBlockPO
     * @return
     * @author zhongml [2020/4/24]
     */
    int updateChallenge(ChallengeBlockPO challengeBlockPO);

    /**
     * 删除一个模块
     *
     * @param blockId
     * @return
     * @author zhongml [2020/4/24]
     */
    int deleteChallenge(Integer blockId);

    /**
     * 批量插入前置模块
     *
     * @param preconditonBlocks
     * @return
     * @author zhongml [2020/4/24]
     */
    int insertConditionBlocks(Integer blockId, List<ChallengeBlockConditionVO> preconditonBlocks);

    /**
     * 根据blockId删除所有前置模块
     *
     * @param blockId
     * @return
     * @author zhongml [2020/4/25]
     */
    int deleteConditions(Integer blockId);

}
