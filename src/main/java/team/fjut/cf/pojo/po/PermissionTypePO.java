package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author axiang [2019/10/18]
 */
@Data
@Table(name = "t_permission_type")
public class PermissionTypePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    String permissionName;
}
