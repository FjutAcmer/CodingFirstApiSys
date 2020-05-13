package team.fjut.cf.pojo.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * 统计用户
 * @author zhongml [2020/5/13]
 */
@Data
public class UserActiveVO {
    private Integer[] active;
    private Integer[] newRegister;
}