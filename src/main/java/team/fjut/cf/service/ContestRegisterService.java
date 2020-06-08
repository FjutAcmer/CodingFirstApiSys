package team.fjut.cf.service;

import team.fjut.cf.pojo.po.ContestRegisterUserPO;
import team.fjut.cf.pojo.vo.ContestRegisterUserVO;

import java.util.List;

/**
 * @author zhongml [2020/4/23]
 */
public interface ContestRegisterService {

    /**
     * 根据条件分页查询用户报名列表
     *
     * @param page
     * @param limit
     * @param sort
     * @param contestKind
     * @param reviewStatus
     * @param username
     * @return
     */
    List<ContestRegisterUserVO> pagesByConditions(Integer page, Integer limit, String sort,
                                                  Integer contestKind, Integer reviewStatus, String username, Integer contestId);

    /**
     * 根据条件查询用户报名列表数量
     *
     * @param contestKind
     * @param reviewStatus
     * @param username
     * @return
     */
    Integer selectCountByConditions(Integer contestKind, Integer reviewStatus, String username);

    /**
     * 修改用户报名审核状态
     *
     * @param contestRegisterUserPO
     * @return
     */
    Integer updateReviewStatus(ContestRegisterUserPO contestRegisterUserPO);

}
