package team.fjut.cf.pojo.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhongml [2020/5/7]
 */
@Data
public class ContestInfoVO {
    private Integer id;
    private String title;
    private Integer contestKind;
    private Date beginTime;
    private Date endTime;
    private Date registerBeginTime;
    private Date registerEndTime;
    private Integer permissionType;
    private List<Integer> problems;
    private String description;
    private String password;
    private Integer computerRating;
    private Integer rankType;
    private Integer problemPutTag;
    private Integer statusReadOut;
    private Integer showRegisterList;
    private Integer showBorderList;
    private Integer showOtherStatus;
    private String createUser;
    private String createTime;
}
