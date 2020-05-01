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
     * @author zhongml [2020/4/29]
     * 查询用户拥有的权限
     *
     * @param username
     * @return
     */
    List<UserPermission> selectUserPermission(String username);

    /**
     * @author zhongml [2020/4/29]
     * 查询所有权限
     *
     * @return
     */
    List<PermissionTypePO> selectAllPermission();

    /**
     * @author zhongml [2020/4/29]
     * 条件查询拥有管理员权限的用户
     *
     * @param username
     * @return
     */
    List<UserPermissionVO> pageByCondition(Integer pageNum, Integer pageSize, String sort, String username);

    /**
     * @author zhongml [2020/4/29]
     * 条件查询拥有管理员权限的用户数量
     *
     * @param username
     * @return
     */
    int countByCondition(String username);

    /**
     * @author zhongml [2020/4/30]
     * 授权用户权限
     *
     * @param username
     * @param granter
     * @param permissionIds
     * @return
     */
    int grantPermissions(String username, String granter, List<Integer> permissionIds);

    /**
     * @author zhongml [2020/4/30]
     * 取消用户权限
     *
     * @param username
     * @param deletePermissions
     * @return
     */
    int revokePermissions(String username, List<UserPermission> deletePermissions);

    /**
     * @author zhongml [2020/4/29]
     * 授权管理员权限
     *
     * @param username
     * @param granter
     * @return
     */
    int grantAdmin(String username, String granter);

    /**
     * @author zhongml [2020/4/29]
     * 取消管理员权限
     *
     * @param username
     * @return
     */
    int revokeAdmin(String username);

    /**
     * @author zhongml [2020/4/29]
     * 是否拥有相应权限
     *
     * @param username
     * @param permissionId
     * @return
     */
    boolean isUserHasThisPermission(String username, Integer permissionId);
}
