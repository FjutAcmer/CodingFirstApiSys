package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author axiang [2019/10/18]
 */
@Data
@Table(name = "t_contest_register_user")
public class ContestRegisterUserPO {
    private Integer id;
    private Integer contestId;
    private String username;
    private Date registerTime;
    private Integer reviewStatus;
    private String reviewInfo;
    private Date reviewTime;
}
