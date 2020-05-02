package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.mapper.ProblemInfoMapper;
import team.fjut.cf.mapper.ProblemViewMapper;
import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.po.ProblemView;
import team.fjut.cf.service.ProblemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/3/6]
 */
@Service
public class ProblemInfoServiceImpl implements ProblemInfoService {
    @Resource
    ProblemInfoMapper problemInfoMapper;

    @Resource
    ProblemViewMapper problemViewMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProblemInfoToSim selectInfoToSimById(Integer id) {
        ProblemInfo problemInfo = problemInfoMapper.selectByPrimaryKey(id);
        Example example = new Example(ProblemView.class);
        example.createCriteria().andEqualTo("problemId", problemInfo.getProblemId());
        ProblemView problemView = problemViewMapper.selectOneByExample(example);
        ProblemInfoToSim problemInfoToSim = new ProblemInfoToSim();
        problemInfoToSim.setTitle(problemInfo.getTitle());
        problemInfoToSim.setDescription(problemView.getDescription());
        problemInfoToSim.setInput(problemView.getInput());
        problemInfoToSim.setOutput(problemView.getOutput());
        return problemInfoToSim;
    }

    @Override
    public ProblemInfo selectProblemInfo(Integer problemId) {
        Example example = new Example(ProblemInfo.class);
        example.createCriteria().andEqualTo("problemId", problemId);
        return problemInfoMapper.selectOneByExample(example);
    }

    // add by zhongml [2020/4/17]
    @Override
    public int deleteProblem(Integer problemId) {
        Example example = new Example(ProblemInfo.class);
        example.createCriteria().andEqualTo("problemId", problemId);
        return problemInfoMapper.deleteByExample(example);
    }

    // add by zhongml [2020/4/17]
    @Override
    public List<ProblemInfo> selectAll() {
        return problemInfoMapper.selectAll();
    }


}
