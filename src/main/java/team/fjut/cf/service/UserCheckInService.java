package team.fjut.cf.service;


import team.fjut.cf.pojo.po.UserCheckIn;

import java.util.List;

/**
 * @author axiang [2019/10/30]
 */
public interface UserCheckInService {
    /**
     * 插入一条签到记录
     *
     * @param userCheckIn
     * @return
     */
    Integer insert(UserCheckIn userCheckIn);

    /**
     * 查询用户签到记录
     *
     * @param username
     * @return
     */
    List<UserCheckIn> select(String username);

    /**
     * 根据用户名查询用户今天的签到次数
     *
     * @param username
     * @return
     */
    boolean isTodayUserCheckIn(String username);

    /**
     * 分页查询用户的签到记录
     *
     * @param username
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<UserCheckIn> pagesByUsername(String username, Integer pageNum, Integer pageSize);

    /**
     * 分页查询用户的签到记录
     *
     * @param username
     * @return
     */
    Integer countByUsername(String username);

}
