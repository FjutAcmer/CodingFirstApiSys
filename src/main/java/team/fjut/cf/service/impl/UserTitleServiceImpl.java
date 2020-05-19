package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.*;
import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.po.UserTitleRecordPO;
import team.fjut.cf.pojo.vo.UserTitleInfoVO;
import team.fjut.cf.service.UserTitleService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhongml [2020/4/28]
 */
@Service
public class UserTitleServiceImpl implements UserTitleService {
    @Resource
    UserTitleMapper userTitleMapper;

    @Resource
    UserTitleRecordMapper userTitleRecordMapper;

    @Override
    public List<UserTitle> selectAll() {
        return userTitleMapper.selectAll();
    }

    @Override
    public List<UserTitle> pageByCondition(Integer pageNum, Integer pageSize, String sort, String name) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(UserTitle.class);
        if(sort != null && sort.equals("descending")) {
            example.orderBy("id").desc();
        }
        else {
            example.orderBy("id").asc();
        }
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(name)) {
            criteria.andLike("name", name);
        }
        return userTitleMapper.selectByExample(example);
    }

    @Override
    public int countByCondition(String name) {
        Example example = new Example(UserTitle.class);
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(name)) {
            criteria.andLike("name", name);
        }
        return userTitleMapper.selectCountByExample(example);
    }

    @Override
    public List<UserTitleInfoVO> selectTitleByUsername(String username) {
        return userTitleMapper.selectByCondition(username);
    }

    @Override
    public int createTitle(UserTitle userTitle) {
        return userTitleMapper.insertSelective(userTitle);
    }

    @Override
    public int deleteTitle(Integer id) {
        Example example = new Example(UserTitle.class);
        example.createCriteria().andEqualTo("id", id);
        return userTitleMapper.deleteByExample(example);
    }
}
