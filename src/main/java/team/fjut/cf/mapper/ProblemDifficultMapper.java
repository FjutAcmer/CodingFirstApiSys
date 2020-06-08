package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.ProblemDifficultPO;
import team.fjut.cf.pojo.po.ProblemTypeCountPO;
import team.fjut.cf.pojo.vo.response.SubmitProblemTypeVO;

import java.util.List;

/**
 * @author axiang [2019/10/22]
 */
public interface ProblemDifficultMapper {
    /**
     * 插入一条难度记录
     *
     * @param problemDifficult
     * @return
     */
    Integer insert(@Param("problemDifficult") ProblemDifficultPO problemDifficult);

    /**
     * 根据题目ID 删除一条难度记录
     *
     * @param problemId
     * @return
     */
    Integer deleteByProblemId(@Param("problemId") Integer problemId);

    /**
     * 查找题目难度信息
     *
     * @return
     */
    List<ProblemDifficultPO> all();

    /**
     * 查询题目类型总计
     *
     * @return
     */
    List<ProblemTypeCountPO> selectCountType();

    /**
     * 提交数最多的题目类型
     *
     * @return
     */
    List<SubmitProblemTypeVO> selectSubmitProblemType();


}
