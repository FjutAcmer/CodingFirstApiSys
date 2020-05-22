package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.mapper.ProblemDifficultMapper;
import team.fjut.cf.mapper.ProblemInfoMapper;
import team.fjut.cf.mapper.ProblemViewMapper;
import team.fjut.cf.pojo.enums.ProblemType;
import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.po.ProblemTypeCountPO;
import team.fjut.cf.pojo.po.ProblemView;
import team.fjut.cf.pojo.vo.response.SubmitProblemTypeVO;
import team.fjut.cf.service.ProblemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    ProblemDifficultMapper problemDifficultMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProblemInfoToSim selectInfoToSimById(int problemId) {
        Example example1 = new Example(ProblemInfo.class);
        example1.createCriteria().andEqualTo("problemId", problemId);
        ProblemInfo problemInfo = problemInfoMapper.selectOneByExample(example1);

        Example example2 = new Example(ProblemView.class);
        example2.createCriteria().andEqualTo("problemId", problemInfo.getProblemId());
        ProblemView problemView = problemViewMapper.selectOneByExample(example2);
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

    @Override
    public List<ProblemTypeCountPO> countProblemType() {
        return problemDifficultMapper.selectCountType();
    }

    @Override
    public List<SubmitProblemTypeVO> selectSubmitProblemType() {
        List<SubmitProblemTypeVO> submitProblemTypeVOS = problemDifficultMapper.selectSubmitProblemType();
        for (SubmitProblemTypeVO vo : submitProblemTypeVOS) {
            vo.setProblemTypeName(ProblemType.getNameByID(vo.getProblemTypeId()));
        }
        return submitProblemTypeVOS;
    }


}
