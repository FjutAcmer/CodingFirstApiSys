package team.fjut.cf.service;

import team.fjut.cf.pojo.po.UserAuth;
import team.fjut.cf.pojo.po.UserBaseInfo;
import team.fjut.cf.pojo.po.UserCustomInfo;
import team.fjut.cf.pojo.vo.*;

import java.util.List;

/**
 * @author axiang [2019/10/11]
 */
public interface UserBaseInfoService {
    /**
     * 注册用户
     * @param userBaseInfo
     * @param userAuth
     * @param userCustomInfo
     * @return
     */
    boolean registerUser(UserBaseInfo userBaseInfo, UserAuth userAuth, UserCustomInfo userCustomInfo);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    boolean userLogin(String username, String password);

    /**
     * 根据用户名查询该用户名是否存在
     * 依据是UserBaseInfo表和UserCustomInfo表中各有一条记录
     *
     * @param username
     * @return
     */
    boolean isUserExist(String username);

    /**
     * 根据用户名查找用户所有信息
     *
     * @param username
     * @return
     */
    UserBaseInfo selectByUsername(String username);

    /**
     * 查询ACB榜单
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<UserAcbBorderVO> selectAcbBorder(int pageNum, int pageSize);

    /**
     * 查询AC题数榜单
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<UserAcNumBorderVO> selectAcNumBorder(int pageNum, int pageSize);

    /**
     * 查询积分榜单
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<UserRatingBorderVO> selectRatingBorder(int pageNum, int pageSize);

    /**
     * @author zhongml [2020/4/28]
     * 条件查询用户信息列表
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @return
     */
    List<UserInfoAdminVO> pageByCondition(Integer pageNum, Integer pageSize, String username);

    /**
     * @author zhongml [2020/4/28]
     * 条件查询用户列表数量
     *
     * @param username
     * @return
     */
    int countByCondition(String username);

    /**
     * @author zhongml [2020/5/8]
     * 更新ACB
     *
     * @param username
     * @param ACB
     * @return
     */
    int updateACB(String username, Integer ACB);
}
