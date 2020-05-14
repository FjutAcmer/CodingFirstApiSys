package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.UserActivePO;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhongml [2020/5/12]
 */
public interface UserActiveMapper extends Mapper<UserActivePO> {

    /**
     * 查询某天用户活跃量
     *
     * @param time
     * @return
     */
    Integer selectCountByDate(@Param("time") String time);
}
