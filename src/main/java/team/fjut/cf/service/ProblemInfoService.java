package team.fjut.cf.service;

import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.po.ProblemTypeCountPO;
import team.fjut.cf.pojo.vo.response.SubmitProblemTypeVO;

import java.util.List;

/**
 * 题目信息Service
 *
 * @author axiang
 */
public interface ProblemInfoService {


    /**
     * 拿出题目的查重数据集
     *
     * @param problemId
     * @return
     */
    ProblemInfoToSim selectInfoToSimById(int problemId);

    /**
     * 根据题目ID查询题目基本信息
     *
     * @param problemId
     * @return
     */
    ProblemInfo selectProblemInfo(Integer problemId);

    /**
     * 根据题目ID删除题目
     *
     * @param problemId
     * @return
     * @author zhongml [2020/4/17]
     */
    int deleteProblem(Integer problemId);

    /**
     * 查询所有题目
     *
     * @return
     * @author zhongml [2020/4/17]
     */
    List<ProblemInfo> selectAll();

    /**
     * @author zhongml [2020/5/14]
     * 统计submit最多的题目类型
     *
     * @return
     */
    List<SubmitProblemTypeVO> selectSubmitProblemType();

    /**
     * @author zhongml [2020/5/14]
     * 统计题库的题目类型
     *
     * @return
     */
    List<ProblemTypeCountPO> countProblemType();

}
