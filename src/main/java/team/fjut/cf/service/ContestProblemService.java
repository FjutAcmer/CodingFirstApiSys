package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ContestProblemPO;
import team.fjut.cf.pojo.vo.response.ContestProblemVO;

import java.util.List;

/**
 * @author axiang [2019/11/21]
 */
public interface ContestProblemService {
    /**
     * 根据比赛ID查询比赛题目
     *
     * @param contestId
     * @return
     */
    List<ContestProblemVO> selectByContestId(Integer contestId);

    /**
     * 添加比赛题目
     *
     * @param problems
     * @param contestId
     * @return
     */
    Integer insertContestProblem(List<Integer> problems, Integer contestId);

    /**
     * 修改比赛题目
     *
     * @param problems
     * @param contestId
     * @return
     */
    Integer updateContestProblem(List<Integer> problems, Integer contestId);

}
