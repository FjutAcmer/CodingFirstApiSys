package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author axiang [2019/11/12]
 */
@Table(name = "t_user_title_record")
@Data
public class UserTitleRecordPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    String username;
    Integer titleId;
    Date obtainTime;
    Date expiredTime;
}
