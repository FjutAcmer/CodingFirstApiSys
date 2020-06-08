package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.UserProblemSolvedMapper;
import team.fjut.cf.pojo.po.UserProblemSolved;
import team.fjut.cf.service.UserProblemSolvedService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author axiang [2019/11/15]
 */
@Service
public class UserProblemSolvedServiceImpl implements UserProblemSolvedService {
    @Resource
    UserProblemSolvedMapper userProblemSolvedMapper;

    @Override
    public UserProblemSolved selectUserSolved(String username, Integer problemId) {
        Example example = new Example(UserProblemSolved.class);
        example.createCriteria().andEqualTo("username", username)
                .andEqualTo("problemId", problemId);
        return userProblemSolvedMapper.selectOneByExample(example);
    }
}
