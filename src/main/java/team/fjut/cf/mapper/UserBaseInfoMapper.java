package team.fjut.cf.mapper;

import team.fjut.cf.pojo.po.UserBaseInfo;
import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.vo.UserInfoAdminVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/10/11]
 */
public interface UserBaseInfoMapper extends Mapper<UserBaseInfo> {


    /**
     * 根据用户名查找用户基本信息
     *
     * @param username
     * @return
     */
    UserBaseInfo selectByUsername(@Param("username") String username);

    /**
     * 查询ACB榜单
     *
     * @return
     */
    List<UserBaseInfo> allAcbTop();

    /**
     * 查询AC题数榜单
     *
     * @return
     */
    List<UserBaseInfo> allAcNumTop();

    /**
     * 查询积分榜单
     *
     * @return
     */
    List<UserBaseInfo> allRatingTop();

    /**
     * @author zhongml [2020/4/28]
     * 条件查询用户信息
     *
     * @param username
     * @return
     */
    List<UserInfoAdminVO> selectByCondition(@Param("username") String username, @Param("sort") String sort, @Param("sortItem") String sortItem);

    /**
     * @author zhongml [2020/4/28]
     * 条件查询用户信息数量
     *
     * @param username
     * @return
     */
    int selectCountByCondition(@Param("username") String username);

    /**
     * @author zhongml [2020/5/8]
     * 更新ACB
     *
     * @param username
     * @param ACB
     * @return
     */
    int updateACB(@Param("username") String username, @Param("ACB") Integer ACB);

    /**
     * 查询某天新用户量
     *
     * @param time
     * @return
     */
    int selectCountByDate(@Param("time") String time);
}
