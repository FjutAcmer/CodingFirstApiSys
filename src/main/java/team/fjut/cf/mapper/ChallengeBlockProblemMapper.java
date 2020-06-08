package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.ChallengeBlockProblemPO;
import team.fjut.cf.pojo.vo.ChallengeBlockProblemAdminVO;
import team.fjut.cf.pojo.vo.ChallengeBlockProblemVO;
import team.fjut.cf.pojo.vo.UserChallengeBlockVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/11/11]
 */
public interface ChallengeBlockProblemMapper extends Mapper<ChallengeBlockProblemPO> {

    /**
     * 查询模块内的题目
     *
     * @param blockId
     * @return
     */
    List<ChallengeBlockProblemVO> allAsVO(@Param("blockId") Integer blockId);

    /**
     * 查询模块内题目的总数量
     *
     * @param blockId
     * @return
     */
    Integer allCount(@Param("blockId") Integer blockId);

    /**
     * 查询用户模块解锁分数
     *
     * @param username
     * @return
     */
    List<UserChallengeBlockVO> selectScoredByUsername(@Param("username") String username);

    /**
     * 查询题目隶属哪个挑战模式模块
     *
     * @param problemId
     * @return
     */
    Integer selectByProblemId(@Param("problemId") Integer problemId);

    /**
     * 查询用户在某个模块下的得分情况
     *
     * @param username
     * @param blockId
     * @return
     */
    Integer selectScoredByBlockIdAndUsername(@Param("blockId") Integer blockId, @Param("username") String username);

    /**
     * 查询模块的全部积分
     *
     * @param blockId
     * @return
     */
    Integer selectTotalScoreByBlockId(@Param("blockId") Integer blockId);

    /**
     * @param blockId
     * @return
     * @author zhongml [2020/4/25]
     * 后台管理查询模块所有题目列表
     */
    List<ChallengeBlockProblemAdminVO> selectProblemsByBlockId(@Param("blockId") Integer blockId);

    /**
     * @param blockId
     * @param challengeProblems
     * @return
     * @author zhongml [2020/4/24]
     * 批量插入模块题目
     */
    Integer insertProblems(@Param("blockId") Integer blockId, @Param("list") List<ChallengeBlockProblemAdminVO> challengeProblems);

}
