package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ProblemStarPO;

import java.util.List;

/**
 * 题目收藏Service
 *
 * @author zhongml [2020/4/17]
 */
public interface ProblemStarService {

    /**
     * 根据题目ID查询用户收藏
     *
     * @param problemId
     * @return
     */
    List<ProblemStarPO> selectStar(Integer problemId);

}
