package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ProblemTagPO;
import team.fjut.cf.pojo.po.ProblemTagRecordPO;

import java.util.List;

/**
 * @author axiang [2019/10/30]
 */
public interface ProblemTagService {
    /**
     * 查询全部题目标签
     *
     * @return
     */
    List<ProblemTagPO> select();

    /**
     * 查询分页题目标签
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param name
     * @return
     * @author zhongml [2020/4/17]
     */
    List<ProblemTagPO> selectByCondition(Integer pageNum, Integer pageSize, String sort, String name);

    /**
     * 添加一条题目标签
     *
     * @param problemTag
     * @return
     * @author zhongml [2020/4/20]
     * <p>
     * *
     */
    int createTag(ProblemTagPO problemTag);

    /**
     * 根据ID修改题目标签
     *
     * @param problemTag
     * @return
     * @author zhongml [2020/4/20]
     * <p>
     * *
     */
    int updateTag(ProblemTagPO problemTag);

    /**
     * 删除一条题目标签
     *
     * @param id
     * @return
     * @author zhongml [2020/4/20]
     * <p>
     * *
     */
    int deleteTag(Integer id);

    /**
     * 查询题目标签数量
     *
     * @param name
     * @return
     * @author zhongml [2020/4/20]
     */
    int countByCondition(String name);

    /**
     * 根据题目ID查询用户标签记录
     *
     * @param problemId
     * @return
     * @author zhongml [2020/4/17]
     */
    List<ProblemTagRecordPO> selectTagRecord(Integer problemId);

}
