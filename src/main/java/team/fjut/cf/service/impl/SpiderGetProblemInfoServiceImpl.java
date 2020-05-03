package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.mapper.ProblemInfoMapper;
import team.fjut.cf.mapper.ProblemSampleMapper;
import team.fjut.cf.mapper.ProblemViewMapper;
import team.fjut.cf.mapper.SpiderGetProblemInfoMapper;
import team.fjut.cf.pojo.po.ProblemInfo;
import team.fjut.cf.pojo.po.ProblemSample;
import team.fjut.cf.pojo.po.ProblemView;
import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.transform.TransSpiderGetProblemInfo;
import team.fjut.cf.pojo.vo.request.LocalizedProblemVO;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;
import team.fjut.cf.service.SpiderGetProblemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
@Service
@Slf4j
public class SpiderGetProblemInfoServiceImpl implements SpiderGetProblemInfoService {
    @Resource
    SpiderGetProblemInfoMapper spiderGetProblemInfoMapper;

    @Resource
    ProblemInfoMapper problemInfoMapper;

    @Resource
    ProblemSampleMapper problemSampleMapper;

    @Resource
    ProblemViewMapper problemViewMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean localizedProblem(LocalizedProblemVO localizedProblemVO) {
        int localNewProblemId = problemInfoMapper.getMaxProblemId() + 1;
        // 插入题目基本信息
        ProblemInfo problemInfo = new ProblemInfo();
        problemInfo.setProblemId(localNewProblemId);
        problemInfo.setTitle(localizedProblemVO.getProblemTitle());
        problemInfo.setVisible(0);
        problemInfo.setJudgeOption(0);
        problemInfo.setBelongProblemId(localizedProblemVO.getProblemId());
        problemInfo.setBelongOjId(0);
        problemInfo.setAuthor("Spider");
        problemInfo.setTotalSubmitUser(0);
        problemInfo.setTotalAcUser(0);
        problemInfo.setTotalAc(0);
        problemInfo.setTotalSubmit(0);
        int i = problemInfoMapper.insertSelective(problemInfo);
        // 插入题目样例集
        ProblemSample problemSample = new ProblemSample();
        problemSample.setProblemId(localNewProblemId);
        problemSample.setCaseOrder(0);
        problemSample.setInputCase(localizedProblemVO.getProblemSampleInput());
        problemSample.setOutputCase(localizedProblemVO.getProblemSampleOutput());
        int i1 = problemSampleMapper.insertSelective(problemSample);
        // 插入题目基本信息相关
        ProblemView problemView = new ProblemView();
        problemView.setDescription(localizedProblemVO.getProblemDescription());
        problemView.setTimeLimit(localizedProblemVO.getProblemTimeLimit());
        problemView.setSpj(localizedProblemVO.getSpj());
        problemView.setMemoryLimit(localizedProblemVO.getProblemMemoryLimit());
        problemView.setIntFormat(localizedProblemVO.getIntFormat());
        problemView.setHint(localizedProblemVO.getProblemHint());
        problemView.setProblemId(localNewProblemId);
        problemView.setInput(localizedProblemVO.getProblemInput());
        problemView.setOutput(localizedProblemVO.getProblemOutput());
        int i2 = problemViewMapper.insertSelective(problemView);
        return i == 1 && i1 == 1 && i2 == 1;
    }

    @Override
    public List<SpiderProblemListVO> pages(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(SpiderGetProblemInfo.class);
        example.orderBy("id").desc();
        List<SpiderGetProblemInfo> spiderGetProblemInfos = spiderGetProblemInfoMapper.selectByExample(example);
        return TransSpiderGetProblemInfo.transformToListVO(spiderGetProblemInfos);
    }

    @Override
    public int count() {
        Example example = new Example(SpiderGetProblemInfo.class);
        return spiderGetProblemInfoMapper.selectCountByExample(example);
    }

    @Override
    public SpiderGetProblemInfo selectById(int id) {
        return spiderGetProblemInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public ProblemInfoToSim selectToSimById(int id) {
        SpiderGetProblemInfo spiderGetProblemInfo = spiderGetProblemInfoMapper.selectByPrimaryKey(id);
        ProblemInfoToSim problemInfoToSim = new ProblemInfoToSim();
        problemInfoToSim.setTitle(spiderGetProblemInfo.getProblemTitle());
        problemInfoToSim.setInput(spiderGetProblemInfo.getProblemInput());
        problemInfoToSim.setOutput(spiderGetProblemInfo.getProblemOutput());
        problemInfoToSim.setDescription(spiderGetProblemInfo.getProblemDescription());
        return problemInfoToSim;

    }
}
