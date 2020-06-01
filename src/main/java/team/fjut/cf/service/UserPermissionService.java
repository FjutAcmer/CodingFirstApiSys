package team.fjut.cf.service;

import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.po.PermissionTypePO;
import team.fjut.cf.pojo.po.UserPermission;
import team.fjut.cf.pojo.vo.UserPermissionVO;

import java.util.List;

/**
 * @author axiang [2020/4/16]
 */
public interface UserPermissionService {
    /**
     * 检查用户是否拥有全部权限
     *
     * @param username
     * @param permissions
     * @return
     */
    boolean isUserHaveAllPermissions(String username, PermissionType[] permissions);

    /**
     * 查询用户拥有的权限
     *
     * @param username
     * @return
     * @author zhongml [2020/4/29]
     */
    List<UserPermission> selectUserPermission(String username);

    /**
     * 查询所有权限
     *
     * @return
     * @author zhongml [2020/4/29]
     */
    List<PermissionTypePO> selectAllPermission();

    /**
     * 条件查询拥有管理员权限的用户
     *
     * @param username
     * @return
     * @author zhongml [2020/4/29]
     */
    List<UserPermissionVO> pageByCondition(Integer pageNum, Integer pageSize, String sort, String username);

    /**
     * 条件查询拥有管理员权限的用户数量
     *
     * @param username
     * @return
     * @author zhongml [2020/4/29]
     */
    int countByCondition(String username);

    /**
     * 授权用户权限
     *
     * @param username
     * @param granter
     * @param permissionIds
     * @return
     * @author zhongml [2020/4/30]
     */
    int grantPermissions(String username, String granter, List<Integer> permissionIds);

    /**
     * 取消用户权限
     *
     * @param username
     * @param deletePermissions
     * @return
     * @author zhongml [2020/4/30]
     */
    int revokePermissions(String username, List<UserPermission> deletePermissions);

    /**
     * 授权管理员权限
     *
     * @param username
     * @param granter
     * @return
     * @author zhongml [2020/4/29]
     */
    int grantAdmin(String username, String granter);

    /**
     * 取消管理员权限
     *
     * @param username
     * @return
     * @author zhongml [2020/4/29]
     */
    int revokeAdmin(String username);

    /**
     * 是否拥有相应权限
     *
     * @param username
     * @param permissionId
     * @return
     * @author zhongml [2020/4/29]
     */
    boolean isUserHasThisPermission(String username, Integer permissionId);
}
