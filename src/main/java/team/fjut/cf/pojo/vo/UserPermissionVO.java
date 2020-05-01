package team.fjut.cf.pojo.vo;

import lombok.Data;
import java.util.Date;

/**
 * @author zhongml [2020/4/29]
 */
@Data
public class UserPermissionVO {
    Integer id;
    String username;
    String permission;
    String granter;
    Date grantTime;
    Integer permissionNum;
}
