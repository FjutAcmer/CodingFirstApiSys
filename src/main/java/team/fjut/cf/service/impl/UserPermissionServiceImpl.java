package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.mapper.PermissionTypeMapper;
import team.fjut.cf.mapper.UserPermissionMapper;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.po.PermissionTypePO;
import team.fjut.cf.pojo.po.UserPermission;
import team.fjut.cf.pojo.vo.UserPermissionVO;
import team.fjut.cf.service.UserPermissionService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2020/4/16]
 */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class UserPermissionServiceImpl implements UserPermissionService {
    @Resource
    UserPermissionMapper userPermissionMapper;

    @Resource
    PermissionTypeMapper permissionTypeMapper;

    @Override
    public boolean isUserHaveAllPermissions(String username, PermissionType[] needPermissions) {
        Example example = new Example(UserPermission.class);
        example.createCriteria().andEqualTo("username", username);
        List<UserPermission> havePermissions = userPermissionMapper.selectByExample(example);
        ArrayList<PermissionType> list = new ArrayList<>();
        for (UserPermission havePermission : havePermissions) {
            list.add(PermissionType.getEnumByID(havePermission.getPermissionId()));
        }
        // 如果没有基础权限，则认定非管理员
        if (!list.contains(PermissionType.BASE_ADMIN)) {
            return false;
        }
        for (PermissionType needPermission : needPermissions) {
            if (!list.contains(needPermission)) {
                return false;
            }
        }
        return true;
    }


    @Override
    public List<UserPermission> selectUserPermission(String username) {
        Example example = new Example(UserPermission.class);
        example.createCriteria().andEqualTo("username", username);
        return userPermissionMapper.selectByExample(example);
    }


    @Override
    public List<PermissionTypePO> selectAllPermission() {
        Example example = new Example(PermissionTypePO.class);
        example.orderBy("id").asc();
        return permissionTypeMapper.selectByExample(example);
    }


    @Override
    public List<UserPermissionVO> pageByCondition(Integer pageNum, Integer pageSize, String sort, String username) {
        List<UserPermissionVO> results = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(UserPermission.class);
        if (sort != null && sort.equals("ascending")) {
            example.orderBy("grantTime").asc();
        } else {
            example.orderBy("grantTime").desc();
        }
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(username)) {
            criteria.andLike("username", username);
        }
        // 查询拥有基本管理员权限的用户
        criteria.andEqualTo("permissionId", 0);

        List<UserPermission> userPermissions = userPermissionMapper.selectByExample(example);
        for (UserPermission userPermission : userPermissions) {
            UserPermissionVO vo = new UserPermissionVO();
            vo.setId(userPermission.getId());
            vo.setUsername(userPermission.getUsername());
            vo.setPermission(PermissionType.getNameByID(userPermission.getPermissionId()));
            vo.setGrantTime(userPermission.getGrantTime());
            vo.setGranter(userPermission.getGranter());
            Example examp = new Example(UserPermission.class);
            examp.createCriteria().andEqualTo("username", userPermission.getUsername());
            Integer permissionNum = userPermissionMapper.selectCountByExample(examp);
            vo.setPermissionNum(permissionNum);
            results.add(vo);
        }
        return results;
    }

    @Override
    public int countByCondition(String username) {
        Example example = new Example(UserPermission.class);
        if (Objects.nonNull(username)) {
            example.createCriteria().andLike("username", username);
        }
        // 查询拥有基本管理员权限的用户
        example.createCriteria().andEqualTo("permissionId", 0);

        return userPermissionMapper.selectCountByExample(example);
    }

    @Override
    public int grantPermissions(String username, String granter, List<Integer> permissionIds) {
        return userPermissionMapper.insertPermissions(username, granter, permissionIds);
    }

    @Override
    public int revokePermissions(String username, List<UserPermission> deletePermissions) {
        return userPermissionMapper.deletePermissions(username, deletePermissions);
    }

    @Override
    public int grantAdmin(String username, String granter) {
        List<PermissionTypePO> permissionTypes = permissionTypeMapper.selectAll();
        return userPermissionMapper.insertAllPermissions(username, granter, permissionTypes);
    }


    @Override
    public boolean isUserHasThisPermission(String username, Integer permissionId) {
        Example example = new Example(UserPermission.class);
        example.createCriteria().andEqualTo("username", username);
        example.createCriteria().andEqualTo("permissionId", permissionId);
        List<UserPermission> hasPermission = userPermissionMapper.selectByExample(example);
        return hasPermission.size() != 0;
    }


    @Override
    public int revokeAdmin(String username) {
        Example example = new Example(UserPermission.class);
        example.createCriteria().andEqualTo("username", username);
        return userPermissionMapper.deleteByExample(example);
    }
}
