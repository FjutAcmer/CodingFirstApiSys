package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.ContestInfoPO;
import team.fjut.cf.pojo.vo.response.ContestTypeVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/11/18]
 */
public interface ContestInfoMapper extends Mapper<ContestInfoPO> {
    /**
     * 根据条件查询不同类型比赛列表
     *
     * @param kind
     * @param searchTitle
     * @param searchPermission
     * @param searchStatus
     * @return
     */
    List<ContestInfoPO> selectByConditions(@Param("kind") Integer kind,
                                           @Param("searchTitle") String searchTitle,
                                           @Param("searchPermission") Integer searchPermission,
                                           @Param("searchStatus") Integer searchStatus);

    /**
     * 根据条件查询不同比赛类型记录数
     *
     * @param kind
     * @param searchTitle
     * @param searchPermission
     * @param searchStatus
     * @return
     */
    Integer selectCountByConditions(@Param("kind") Integer kind,
                                    @Param("searchTitle") String searchTitle,
                                    @Param("searchPermission") Integer searchPermission,
                                    @Param("searchStatus") Integer searchStatus);


    /**
     * 根据比赛ID查询比赛信息
     *
     * @param contestId
     * @return
     */
    ContestInfoPO selectByContestId(@Param("contestId") Integer contestId);

    /**
     * 查询正在进行的比赛总数
     *
     * @return
     */
    Integer selectCountInProgress();

    /**
     * 统计比赛类型数
     *
     * @return
     * @author zhongml [2020/5/14]
     */
    List<ContestTypeVO> selectContestTypeCount();

}
