package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import team.fjut.cf.mapper.UserCheckInMapper;
import team.fjut.cf.pojo.po.UserCheckIn;
import team.fjut.cf.service.UserCheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fjut.cf.utils.TimeUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author axiang [2019/10/30]
 */
@Service
public class UserCheckInServiceImpl implements UserCheckInService {
    @Resource
    private UserCheckInMapper userCheckInMapper;

    @Override
    public Integer insert(UserCheckIn userCheckIn) {
        return userCheckInMapper.insertSelective(userCheckIn);
    }

    @Override
    public List<UserCheckIn> select(String username) {
        Example example = new Example(UserCheckIn.class);
        example.orderBy("checkTime").desc();
        example.createCriteria().andEqualTo("username", username);
        return userCheckInMapper.selectByExample(example);
    }

    @Override
    public boolean isTodayUserCheckIn(String username) {
        Example example = new Example(UserCheckIn.class);
        example.createCriteria().andEqualTo("username", username)
                .andGreaterThanOrEqualTo("checkTime", TimeUtils.initDateByDay())
                .andLessThanOrEqualTo("checkTime", new Date());
        return userCheckInMapper.selectCountByExample(example) == 1;
    }

    @Override
    public List<UserCheckIn> pagesByUsername(String username, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserCheckIn> userCheckIns = userCheckInMapper.selectByUsername(username);
        return userCheckIns;
    }

    @Override
    public Integer countByUsername(String username) {
        Example example = new Example(UserCheckIn.class);
        example.createCriteria().andEqualTo("username", username);
        return userCheckInMapper.selectCountByExample(example);
    }


}
