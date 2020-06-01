package team.fjut.cf.controller.admin;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.PermissionTypePO;
import team.fjut.cf.pojo.po.UserPermission;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.UserPermissionVO;
import team.fjut.cf.pojo.vo.request.NewUserPermissionVO;
import team.fjut.cf.service.UserPermissionService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhongml [2020/4/29]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/permission")
public class PermissionManagerController {
    @Resource
    UserPermissionService userPermissionService;

    /**
     * 获取管理员列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param username
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.ADMIN_LIST_QUERY})
    @GetMapping("/get_admin")
    public ResultJson getAdminLimit(@RequestParam("page") Integer pageNum,
                                    @RequestParam("limit") Integer pageSize,
                                    @RequestParam(value = "sort", required = false) String sort,
                                    @RequestParam(value = "username", required = false) String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (!StringUtils.isEmpty(username)) {
            username = "%" + username + "%";
        } else {
            username = null;
        }
        List<UserPermissionVO> adminList = userPermissionService.pageByCondition(pageNum, pageSize, sort, username);
        int total = userPermissionService.countByCondition(username);
        resultJson.addInfo(adminList);
        resultJson.addInfo(total);
        return resultJson;
    }

    /**
     * 获取用户权限列表
     *
     * @param username
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.ADMIN_LIST_QUERY})
    @GetMapping("/list")
    public ResultJson getAdminPermission(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<UserPermission> adminPermissionList = userPermissionService.selectUserPermission(username);
        resultJson.addInfo(adminPermissionList);
        return resultJson;
    }

    /**
     * 获取权限类型
     *
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.ADMIN_LIST_QUERY})
    @GetMapping("/type")
    public ResultJson getPermissionType() {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        List<PermissionTypePO> permissionTypes = userPermissionService.selectAllPermission();
        resultJson.addInfo(permissionTypes);
        return resultJson;
    }

    /**
     * 更新权限列表
     *
     * @param vo
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.MODIFY_PERMISSION})
    @RequestMapping("/update_permission")
    public ResultJson updatePermission(@RequestBody NewUserPermissionVO vo) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        // 需要删除的用户权限
        List<UserPermission> deletePermissions = new ArrayList<>();
        List<Integer> permissionIds = vo.getCheckedPermissions();
        List<UserPermission> userPermissions = userPermissionService.selectUserPermission(vo.getUsername());
        for (int i = 0; i < userPermissions.size(); i++) {
            // 判断新的权限列表是否移除了用户原有的权限
            boolean isExist = false;
            for (Integer id : permissionIds) {
                if (userPermissions.get(i).getPermissionId().equals(id)) {
                    isExist = true;
                    break;
                }
            }
            // 是则在原有权限列表里移除该权限记录，并将移除的权限加入需要删除的权限列表中
            if (!isExist) {
                deletePermissions.add(userPermissions.get(i));
                userPermissions.remove(i);
                // 索引减一 否则继续循环将出错
                i--;
            }
        }
        for (int i = 0; i < userPermissions.size(); i++) {
            // 判断原有权限列表和新的权限列表是否有重复，是则将其移除
            if (permissionIds.contains(userPermissions.get(i).getPermissionId())) {
                permissionIds.remove(userPermissions.get(i).getPermissionId());
                i--;
            }
        }
        if (permissionIds.size() != 0) {
            int updateResult = userPermissionService.grantPermissions(vo.getUsername(), vo.getGranter(), permissionIds);
            if (updateResult == 0) {
                resultJson.setStatus(ResultCode.BUSINESS_FAIL, "更新权限失败！");
            }
        }
        if (deletePermissions.size() != 0) {
            int deleteResult = userPermissionService.revokePermissions(vo.getUsername(), deletePermissions);
            if (deleteResult == 0) {
                resultJson.setStatus(ResultCode.BUSINESS_FAIL, "移除权限失败！");
            }
        }
        return resultJson;
    }

    /**
     * 授予管理员
     *
     * @param username
     * @param granter
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.MODIFY_PERMISSION})
    @PostMapping("/grant_admin")
    public ResultJson grantAdmin(@RequestParam("username") String username,
                                 @RequestParam("granter") String granter) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = 0;
        // 是否已拥有基本管理员权限
        boolean isAdmin = userPermissionService.isUserHasThisPermission(username, 0);
        if (isAdmin) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "该用户已经是管理员！");
        } else {
            result = userPermissionService.grantAdmin(username, granter);
            if (result == 0) {
                resultJson.setStatus(ResultCode.BUSINESS_FAIL, "授权管理员失败！");
            }
        }
        return resultJson;
    }

    /**
     * 收回管理员权限
     *
     * @param username
     * @return
     */
    @PermissionRequired(permissions = {
            PermissionType.MODIFY_PERMISSION,
            PermissionType.REMOVE_ADMIN})
    @DeleteMapping("/revoke_admin")
    public ResultJson grantAdmin(@RequestParam("username") String username) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = userPermissionService.revokeAdmin(username);
        if (result == 0) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "移除管理员失败！");
        }
        return resultJson;
    }

}
