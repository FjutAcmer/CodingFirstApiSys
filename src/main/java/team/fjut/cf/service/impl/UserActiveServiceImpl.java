package team.fjut.cf.service.impl;

import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.UserActiveMapper;
import team.fjut.cf.pojo.po.UserActivePO;
import team.fjut.cf.pojo.vo.response.UserActiveVO;
import team.fjut.cf.service.UserActiveService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserActiveServiceImpl implements UserActiveService {
    @Resource
    UserActiveMapper userActiveMapper;

    @Override
    public Integer[] getUserActive(List<String> pastDaysList) {
        Integer[] userActive = new Integer[7];
        // 获取过去7天对应数据
        for (int i = 0; i < 7; i ++) {
            userActive[i] = userActiveMapper.selectCountByDate(pastDaysList.get(i));
        }
        return userActive;
    }
}
