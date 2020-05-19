package team.fjut.cf.service.impl;

import team.fjut.cf.mapper.ContestProblemMapper;
import team.fjut.cf.pojo.po.ContestProblemPO;
import team.fjut.cf.service.ContestProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2019/11/21]
 */
@Service
public class ContestProblemServiceImpl implements ContestProblemService {
    @Resource
    ContestProblemMapper contestProblemMapper;

    @Override
    public List<ContestProblemPO> selectByContestId(Integer contestId) {
        return contestProblemMapper.selectByContestId(contestId);
    }

    @Override
    public Integer insertContestProblem(List<Integer> problems, Integer contestId) {
        return contestProblemMapper.insertProblems(problems, contestId);
    }

    @Override
    public Integer updateContestProblem(List<Integer> problems, Integer contestId) {
        Integer result = 1;
        Example example = new Example(ContestProblemPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("contestId",contestId);
        Integer result1 = contestProblemMapper.deleteByExample(example);
        Integer result2 = contestProblemMapper.insertProblems(problems, contestId);
        if (result1 == 0 || result2 ==0) {
            result = 0;
        }
        return  result;
    }
}
