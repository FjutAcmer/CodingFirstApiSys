package team.fjut.cf.mapper;

import org.apache.catalina.User;
import team.fjut.cf.pojo.po.PermissionTypePO;
import team.fjut.cf.pojo.po.UserPermission;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/10/18]
 */
public interface UserPermissionMapper extends Mapper<UserPermission> {

    /**
     * @author zhongml [2020/4/30]
     * @param username
     * @param granter
     * @param permissionTypes
     * @return
     */
    int insertAllPermissions(@Param("username") String username,
                             @Param("granter") String granter,
                             @Param("permissions") List<PermissionTypePO> permissionTypes);

    /**
     * @author zhongml [2020/4/30]
     * @param userPermissions
     * @return
     */
    int deletePermissions(@Param("username") String username, @Param("userPermissions") List<UserPermission> userPermissions);

    /**
     * @author zhongml [2020/4/30]
     * @param username
     * @param granter
     * @param permissionIds
     * @return
     */
    int insertPermissions(@Param("username") String username,
                             @Param("granter") String granter,
                             @Param("permissionIds") List<Integer> permissionIds);

}
