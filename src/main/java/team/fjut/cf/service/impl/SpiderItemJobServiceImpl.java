package team.fjut.cf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SpiderItemJobMapper;
import team.fjut.cf.pojo.po.SpiderItemJob;
import team.fjut.cf.service.SpiderItemJobService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

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
}
