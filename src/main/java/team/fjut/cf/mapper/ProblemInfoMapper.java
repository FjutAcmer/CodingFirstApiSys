package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.ProblemInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/10/22]
 */
public interface ProblemInfoMapper extends Mapper<ProblemInfo> {

    int getMaxProblemId();

    /**
     * 查询用户未解决的题目列表
     *
     * @param username
     * @return
     */
    List<ProblemInfo> selectUnSolvedProblemsByUsername(@Param("username") String username);

}
