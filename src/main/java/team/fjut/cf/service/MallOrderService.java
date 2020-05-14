package team.fjut.cf.service;

import team.fjut.cf.pojo.po.MallOrderPO;
import team.fjut.cf.pojo.vo.MallOrderVO;
import team.fjut.cf.pojo.vo.response.OrderNewAndCancelVO;

import java.util.List;

/**
 * @author zhongml [2020/4/22]
 */
public interface MallOrderService {

    /**
     * 条件查询商品列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param id
     * @param orderUser
     * @param orderStatus
     * @param orderCancel
     * @return
     */
    List<MallOrderVO> selectByCondition(Integer pageNum, Integer pageSize, String sort, Integer id, String orderUser, Integer orderStatus, Integer orderCancel);
    /**
     * 条件查询商品数量
     *
     * @param id
     * @param orderUser
     * @param orderStatus
     * @param orderCancel
     * @return
     */
    int countByCondition(Integer id, String orderUser, Integer orderStatus, Integer orderCancel);

    /**
     * 根据ID修改商品信息
     *
     * @param mallGoodsPO
     * @return
     */
    int updateOrder(MallOrderPO mallGoodsPO);

    /**
     * @author zhongml [2020/5/14]
     *
     * 统计新订单与取消订单
     * @return
     */
    OrderNewAndCancelVO countNewAndCancel(List<String> pastDaysList);
}
