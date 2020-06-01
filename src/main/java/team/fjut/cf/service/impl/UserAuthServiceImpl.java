package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.UserAuthMapper;
import team.fjut.cf.pojo.po.UserAuth;
import team.fjut.cf.service.UserAuthService;
import team.fjut.cf.utils.Sha1Utils;
import team.fjut.cf.utils.UUIDUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author axiang [2019/11/28]
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Resource
    UserAuthMapper userAuthMapper;

    @Override
    public Integer selectAttemptNumber(String username) {
        Example example = new Example(UserAuth.class);
        example.createCriteria().andEqualTo("username", username);
        return userAuthMapper.selectOneByExample(example).getAttemptLoginFailCount();
    }

    @Override
    public boolean isUserLocked(String username) {
        Example example = new Example(UserAuth.class);
        example.createCriteria().andEqualTo("username", username);
        UserAuth userAuth = userAuthMapper.selectOneByExample(example);
        return userAuth.getLocked() == 1;
    }

    @Override
    public Date selectUnlockTime(String username) {
        Example example = new Example(UserAuth.class);
        example.createCriteria().andEqualTo("username", username);
        return userAuthMapper.selectOneByExample(example).getUnlockTime();
    }

    @Override
    public int updatePassword(String username, String newPsw) {
        UserAuth userAuth = new UserAuth();
        String salt = UUIDUtils.getUUID32();
        // 加盐密码
        String newPassword = salt + newPsw;
        // 对加盐密码使用SHA1加密
        String encryptedPwd = Sha1Utils.encode(newPassword);
        userAuth.setSalt(salt);
        userAuth.setPassword(encryptedPwd);
        Example example = new Example(UserAuth.class);
        example.createCriteria().andEqualTo("username", username);
        return userAuthMapper.updateByExampleSelective(userAuth, example);
    }
}
