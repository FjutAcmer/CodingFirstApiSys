package team.fjut.cf.mapper;

import team.fjut.cf.pojo.po.ProblemTagPO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/10/30]
 */
public interface ProblemTagMapper extends Mapper<ProblemTagPO> {
    /**
     * 查找全部题目标签类型
     *
     * @return
     */
    List<ProblemTagPO> all();
}
