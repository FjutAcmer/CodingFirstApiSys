package team.fjut.cf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.UserCaptchaMapper;
import team.fjut.cf.pojo.po.UserCaptcha;
import team.fjut.cf.service.UserCaptchaService;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Objects;

/**
 * @author axiang [2020/3/18]
 */
@Service
public class UserCaptchaServiceImpl implements UserCaptchaService {
    @Autowired
    UserCaptchaMapper userCaptchaMapper;

    @Override
    public int updateCaptcha(UserCaptcha userCaptcha) {
        userCaptcha.setCreateTime(new Date());
        // 验证码有效期五分钟
        userCaptcha.setExpireTime(new Date(System.currentTimeMillis() + 1000 * 60));
        Example example = new Example(UserCaptcha.class);
        example.createCriteria().andEqualTo("name", userCaptcha.getName());
        UserCaptcha oldUserCaptcha = userCaptchaMapper.selectOneByExample(example);
        if (Objects.isNull(oldUserCaptcha)) {
            return userCaptchaMapper.insertSelective(userCaptcha);
        } else {
            userCaptcha.setId(oldUserCaptcha.getId());
            return userCaptchaMapper.updateByPrimaryKey(userCaptcha);
        }
    }

    @Override
    public int checkCaptcha(String name, String captchaValue) {
        Example example = new Example(UserCaptcha.class);
        example.createCriteria().andEqualTo("name", name);
        UserCaptcha userCaptcha = userCaptchaMapper.selectOneByExample(example);
        if (userCaptcha.getExpireTime().getTime() < System.currentTimeMillis()) {
            return 2;
        }
        else if (userCaptcha.getCaptchaValue().equals(captchaValue)) {
            return 1;
        } else {
            return 0;
        }

    }
}
