package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.PermissionTypePO;
import team.fjut.cf.pojo.po.UserPermission;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/10/18]
 */
public interface UserPermissionMapper extends Mapper<UserPermission> {

    /**
     * @param username
     * @param granter
     * @param permissionTypes
     * @return
     * @author zhongml [2020/4/30]
     */
    int insertAllPermissions(@Param("username") String username,
                             @Param("granter") String granter,
                             @Param("permissions") List<PermissionTypePO> permissionTypes);

    /**
     * @param userPermissions
     * @return
     * @author zhongml [2020/4/30]
     */
    int deletePermissions(@Param("username") String username, @Param("userPermissions") List<UserPermission> userPermissions);

    /**
     * @param username
     * @param granter
     * @param permissionIds
     * @return
     * @author zhongml [2020/4/30]
     */
    int insertPermissions(@Param("username") String username,
                          @Param("granter") String granter,
                          @Param("permissionIds") List<Integer> permissionIds);

}
