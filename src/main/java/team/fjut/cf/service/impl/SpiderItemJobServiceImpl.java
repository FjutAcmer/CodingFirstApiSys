package team.fjut.cf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SpiderItemJobMapper;
import team.fjut.cf.pojo.po.SpiderItemJob;
import team.fjut.cf.pojo.transform.TransSpiderItemJob;
import team.fjut.cf.pojo.vo.response.SpiderJobListVO;
import team.fjut.cf.service.SpiderItemJobService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
@Slf4j
@Service
public class SpiderItemJobServiceImpl implements SpiderItemJobService {
    @Resource
    SpiderItemJobMapper spiderItemJobMapper;

    @Override
    public int insert(SpiderItemJob spiderItemJob) {
        return spiderItemJobMapper.insertSelective(spiderItemJob);
    }

    @Async
    @Override
    public void updateByJobId(String jobId, SpiderItemJob spiderItemJob) {
        Example example = new Example(SpiderItemJob.class);
        example.createCriteria().andEqualTo("jobId", jobId);
        spiderItemJobMapper.updateByExampleSelective(spiderItemJob, example);
    }

    @Override
    public int updateById(Integer id, SpiderItemJob spiderItemJob) {
        Example example = new Example(SpiderItemJob.class);
        example.createCriteria().andEqualTo("id", id);
        return spiderItemJobMapper.updateByExampleSelective(spiderItemJob, example);
    }

    @Override
    public List<SpiderJobListVO> selectBySpiderName(String spiderName) {
        Example example = new Example(SpiderItemJob.class);
        example.createCriteria().andEqualTo("spiderName", spiderName);
        List<SpiderItemJob> spiderItemJobs = spiderItemJobMapper.selectByExample(example);
        return TransSpiderItemJob.transformToListVO(spiderItemJobs);
    }

    @Override
    public SpiderItemJob selectByJobId(String jobId) {
        Example example = new Example(SpiderItemJob.class);
        example.createCriteria().andEqualTo("jobId", jobId);
        return spiderItemJobMapper.selectOneByExample(example);
    }
}
