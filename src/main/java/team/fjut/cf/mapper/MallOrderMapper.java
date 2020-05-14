package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.MallOrderPO;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhongml [2020/4/22]
 */
public interface MallOrderMapper extends Mapper<MallOrderPO> {

    /**
     * 查询某天新订单
     *
     * @param time
     * @return
     */
    Integer countNewByDate(@Param("time") String time);

    /**
     * 查询某天取消订单
     *
     * @param time
     * @return
     */
    Integer countCancelByDate(@Param("time") String time);

}
