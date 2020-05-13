package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.MallOrderMapper;
import team.fjut.cf.pojo.enums.MallOrderStatus;
import team.fjut.cf.pojo.po.MallOrderPO;
import team.fjut.cf.pojo.vo.MallOrderVO;
import team.fjut.cf.pojo.vo.response.OrderNewAndCancelVO;
import team.fjut.cf.service.MallOrderService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhongml [2020/4/22]
 */
@Service
public class MallOrderServiceImpl implements MallOrderService {
    @Resource
    MallOrderMapper mallOrderMapper;

    @Override
    public List<MallOrderVO> selectByCondition(Integer pageNum, Integer pageSize, String sort, Integer goodsId, String orderUser, Integer orderStatus, Integer orderCancel) {
        PageHelper.startPage(pageNum, pageSize);
        List<MallOrderVO> result = new ArrayList<>();

        Example example = new Example(MallOrderPO.class);
        if(sort != null && sort.equals("descending")) {
            example.orderBy("id").desc();
        }
        else {
            example.orderBy("id").asc();
        }
        Example.Criteria criteria = example.createCriteria();

        if (Objects.nonNull(goodsId)) {
            criteria.andEqualTo("id", goodsId);
        }
        if (Objects.nonNull(orderUser)) {
            criteria.andLike("orderUser", orderUser);
        }
        if (Objects.nonNull(orderStatus)) {
            criteria.andEqualTo("orderStatus", orderStatus);
        }
        if (Objects.nonNull(orderCancel)) {
            criteria.andEqualTo("orderCancel", orderCancel);
        }

        List<MallOrderPO> mallOrderPOS = mallOrderMapper.selectByExample(example);
        for (MallOrderPO item : mallOrderPOS) {
            MallOrderVO mallOrderVO = new MallOrderVO();
            mallOrderVO.setId(item.getId());
            mallOrderVO.setGoodsId(item.getGoodsId());
            mallOrderVO.setOrderUser(item.getOrderUser());
            mallOrderVO.setOrderTime(item.getOrderTime());
            mallOrderVO.setOriginCost(item.getOriginCost());
            mallOrderVO.setOrderCancel(item.getOrderCancel());
            mallOrderVO.setRealCost(item.getRealCost());
            mallOrderVO.setReviewUser(item.getReviewUser());
            mallOrderVO.setOrderStatus(MallOrderStatus.getNameById(item.getOrderStatus()));
            result.add(mallOrderVO);
        }
        return result;
    }

    @Override
    public int countByCondition(Integer goodsId, String orderUser, Integer orderStatus, Integer orderCancel) {
        Example example = new Example(MallOrderPO.class);
        Example.Criteria criteria = example.createCriteria();
        // 非空则查询条件
        if (Objects.nonNull(goodsId)) {
            criteria.andEqualTo("goodsId", goodsId);
        }
        if (Objects.nonNull(orderUser)) {
            criteria.andLike("orderUser", orderUser);
        }
        return mallOrderMapper.selectCountByExample(example);
    }

    @Override
    public int updateOrder(MallOrderPO mallGoodsPO) {
        Example example = new Example(MallOrderPO.class);
        example.createCriteria().andEqualTo("id", mallGoodsPO.getId());
        return mallOrderMapper.updateByExampleSelective(mallGoodsPO, example);
    }

    @Override
    public OrderNewAndCancelVO countNewAndCancel(List<String> pastDaysList) {
        OrderNewAndCancelVO orderNewAndCancelVO = new OrderNewAndCancelVO();
        Integer[] newOrder = new Integer[7];
        Integer[] newCancel = new Integer[7];
        for (int i = 0; i < 7; i ++) {
            newOrder[i] = mallOrderMapper.countNewByDate(pastDaysList.get(i));
            newCancel[i] = mallOrderMapper.countCancelByDate(pastDaysList.get(i));
        }
        orderNewAndCancelVO.setNewOrder(newOrder);
        orderNewAndCancelVO.setNewCancel(newCancel);
        return orderNewAndCancelVO;
    }
}
