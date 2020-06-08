package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.ChallengeBlockConditionPO;
import team.fjut.cf.pojo.vo.response.ChallengeBlockConditionVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/11/11]
 */
public interface ChallengeBlockConditionMapper extends Mapper<ChallengeBlockConditionPO> {
    /**
     * 查询模块之间的关系
     *
     * @return
     */
    List<ChallengeBlockConditionPO> all();


    /**
     * 查询模块解锁条件
     *
     * @param blockId
     * @return
     */
    List<ChallengeBlockConditionVO> selectConditionByBlockId(@Param("blockId") Integer blockId);

    /**
     * 查询后置模块
     *
     * @param blockId
     * @return
     */
    List<Integer> selectRearBlocksByBlockId(@Param("blockId") Integer blockId);

    /**
     * 根据blockId查询前置模块总积分
     *
     * @param blockId
     * @return
     */
    Integer selectBlockConditionTotalScore(@Param("blockId") Integer blockId);

    /**
     * 批量插入前置模块
     *
     * @param blockId
     * @param preconditonBlocks
     * @return
     * @author zhongml [2020/4/24]
     */
    Integer insertConditionBlocks(@Param("blockId") Integer blockId, @Param("list") List<ChallengeBlockConditionVO> preconditonBlocks);
}
