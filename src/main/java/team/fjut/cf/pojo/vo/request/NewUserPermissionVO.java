package team.fjut.cf.pojo.vo.request;

import lombok.Data;
import java.util.List;

/**
 * @author zhongml [2020/4/30]
 */
@Data
public class NewUserPermissionVO {
    String username;
    String granter;
    List<Integer> checkedPermissions;
}
