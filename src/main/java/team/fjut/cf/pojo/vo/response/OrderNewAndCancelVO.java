package team.fjut.cf.pojo.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * 统计新订单与取消订单
 * @author zhongml [2020/5/13]
 */
@Data
public class OrderNewAndCancelVO {
    private Integer[] newOrder;
    private Integer[] newCancel;
}