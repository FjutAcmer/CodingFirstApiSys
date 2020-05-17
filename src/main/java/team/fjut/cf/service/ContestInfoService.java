package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ContestInfoPO;
import team.fjut.cf.pojo.vo.ContestInfoVO;
import team.fjut.cf.pojo.vo.ContestListVO;
import team.fjut.cf.pojo.vo.response.ContestTypeVO;

import java.util.List;

/**
 * @author axiang [2019/11/18]
 */
public interface ContestInfoService {

    /**
     * 根据条件分页查询不同类型比赛列表
     *
     * @param kind
     * @param searchTitle
     * @param searchPermission
     * @param searchStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<ContestListVO> pagesByConditions(Integer kind,
                                          String searchTitle,
                                          Integer searchPermission,
                                          Integer searchStatus,
                                          Integer pageNum,
                                          Integer pageSize);

    /**
     * 根据条件查询不同比赛类型记录数
     *
     * @param kind
     * @param searchTitle
     * @param searchPermission
     * @param searchStatus
     * @return
     */
    Integer selectCountByConditions(Integer kind,
                                    String searchTitle,
                                    Integer searchPermission,
                                    Integer searchStatus);

    /**
     * 根据比赛ID查询比赛信息
     * @param contestId
     * @return
     */
    ContestInfoPO selectByContestId(Integer contestId);

    /**
     * 查询所有比赛
     * @author zhongml [2020/5/7]
     *
     * @return
     */
    List<ContestInfoPO> selectAll();

    /**
     * 管理员新增比赛
     * @author zhongml [2020/4/23]
     *
     * @param contestInfoVO
     * @return
     */
    Integer createContest(ContestInfoVO contestInfoVO);

    /**
     * 管理员修改比赛信息
     * @author zhongml [2020/5/14]
     *
     * @param newContestVO
     * @return
     */
    Integer updateContest(ContestInfoVO newContestVO);


    /**
     * 统计正在进行的比赛
     * @author zhongml [2020/5/12]
     *
     * @return
     */
    Integer countContestInProgress();

    /**
     * 统计比赛类型数
     * @author zhongml [2020/5/14]
     *
     * @return
     */
    List<ContestTypeVO> getContestTypeCount();
}
