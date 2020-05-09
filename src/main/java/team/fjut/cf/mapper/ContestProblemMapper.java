package team.fjut.cf.mapper;

import team.fjut.cf.pojo.po.ContestProblemPO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author axiang [2019/11/21]
 */
public interface ContestProblemMapper {
    /**
     * 根据比赛ID查询比赛题目
     *
     * @param contestId
     * @return
     */
    List<ContestProblemPO> selectByContestId(@Param("contestId") Integer contestId);

    /**
     * 添加比赛题目
     *
     * @param problems
     * @param contestId
     * @return
     */
    Integer insertProblems(@Param("problems") List<Integer> problems, @Param("contestId") Integer contestId);
}
