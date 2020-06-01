package team.fjut.cf.service;

import java.util.Date;

/**
 * 用户权限 service
 *
 * @author axiang [2019/11/28]
 */
public interface UserAuthService {
    /**
     * 查询用户的登录失败次数
     *
     * @param username
     * @return
     */
    Integer selectAttemptNumber(String username);

    /**
     * 查询用户是否锁定
     *
     * @param username
     * @return
     */
    boolean isUserLocked(String username);


    /**
     * 查询用户登录解锁时间
     *
     * @param username
     * @return
     */
    Date selectUnlockTime(String username);

    /**
     * 重置用户密码
     *
     * @param username
     * @param newPsw
     * @return
     * @author zhongml [2020/5/14]
     */
    int updatePassword(String username, String newPsw);
}
