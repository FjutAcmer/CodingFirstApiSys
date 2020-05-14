package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.Table;

/**
 * @author zhongml [2020/05/13]
 */
@Data
@Table(name = "t_user_active")
public class UserActivePO {
    private Integer id;
    private Integer username;
    private Integer loginTime;
}
