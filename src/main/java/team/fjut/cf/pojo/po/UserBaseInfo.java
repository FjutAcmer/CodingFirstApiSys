package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户基础信息实体类
 *
 * @author axiang [2019/10/11]
 */
@Data
@Table(name = "t_user_base_info")
public class UserBaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    String username;
    Integer gender;
    String email;
    String phone;
    String motto;
    Date registerTime;
    Integer rating;
    Integer acb;
    Integer acNum;
}
