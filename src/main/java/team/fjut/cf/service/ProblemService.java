package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.vo.UserRadarVO;

import java.util.List;

/**
 * @author axiang [2019/10/22]
 */
public interface ProblemService {


    /**
     * 查询用户题目雷达图
     *
     * @param username
     * @return
     */
    List<UserRadarVO> selectUserProblemRadarByUsername(String username);

    /**
     * 查询推荐题目列表
     *
     * @param username
     * @return
     */
    List<ProblemInfo> selectRecommendProblemsByUsername(String username);

}
