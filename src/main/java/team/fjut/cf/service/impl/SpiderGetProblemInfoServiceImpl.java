package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.mapper.*;
import team.fjut.cf.pojo.po.*;
import team.fjut.cf.pojo.transform.TransSpiderGetProblemInfo;
import team.fjut.cf.pojo.vo.request.LocalizedProblemVO;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;
import team.fjut.cf.service.SpiderGetProblemInfoService;
import team.fjut.cf.service.SpiderLocalizedRecordService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Resource
    SpiderLocalizedRecordMapper spiderLocalizedRecordMapper;

    @Resource
    SpiderLocalizedRecordService spiderLocalizedRecordService;

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

        SpiderLocalizedRecord spiderLocalizedRecord = new SpiderLocalizedRecord();
        spiderLocalizedRecord.setLocalProblemId(localNewProblemId);
        spiderLocalizedRecord.setSpiderGetProblemId(Integer.parseInt(localizedProblemVO.getProblemId()));
        spiderLocalizedRecord.setCreateUser("SYSTEM");
        int i3 = spiderLocalizedRecordService.insert(spiderLocalizedRecord);
        return i == 1 && i1 == 1 && i2 == 1 && i3 == 1;
    }

    @Override
    public List<SpiderProblemListVO> pages(int pageNum, int pageSize, String spiderJob) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(SpiderGetProblemInfo.class);
        example.orderBy("id").desc();
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(spiderJob)) {
            criteria.andEqualTo("spiderJob", spiderJob);
        }
        List<SpiderGetProblemInfo> spiderGetProblemInfos = spiderGetProblemInfoMapper.selectByExample(example);
        List<SpiderProblemListVO> results = new ArrayList<>();
        for (SpiderGetProblemInfo item : spiderGetProblemInfos) {
            SpiderProblemListVO temp = TransSpiderGetProblemInfo.transformToListVO(item);
            List<SpiderLocalizedRecord> records = spiderLocalizedRecordService.selectByGetProblemId(item.getId());
            if (Objects.isNull(records)) {
                temp.setIsLocalized(null);
            } else {
                temp.setIsLocalized(records.size());
            }
            results.add(temp);
        }
        return results;
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
