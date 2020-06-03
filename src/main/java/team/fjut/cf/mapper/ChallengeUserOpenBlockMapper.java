package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.ChallengeUserOpenBlockPO;

import java.util.List;

/**
 * @author axiang [2019/11/11]
 */
public interface ChallengeUserOpenBlockMapper {
    /**
     * 插入一个解锁记录
     *
     * @param challengeUserOpenBlockPO
     * @return
     */
    Integer insert(@Param("challengeUserOpenBlockPO") ChallengeUserOpenBlockPO challengeUserOpenBlockPO);

    /**
     * 获取用户已开放的挑战模块ID
     *
     * @param username
     * @return
     */
    List<Integer> selectByUsername(@Param("username") String username);
}
