package team.fjut.cf.mapper;

import team.fjut.cf.pojo.po.ContestProblemPO;
import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.vo.response.ContestProblemVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/11/21]
 */
public interface ContestProblemMapper extends Mapper<ContestProblemPO> {
    /**
     * 根据比赛ID查询比赛题目
     *
     * @param contestId
     * @return
     */
    List<ContestProblemVO> selectByContestId(@Param("contestId") Integer contestId);

    /**
     * 添加比赛题目
     *
     * @param problems
     * @param contestId
     * @return
     */
    Integer insertProblems(@Param("problems") List<Integer> problems, @Param("contestId") Integer contestId);
}
