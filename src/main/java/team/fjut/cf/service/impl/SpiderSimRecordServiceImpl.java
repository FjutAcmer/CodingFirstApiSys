package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SpiderSimRecordMapper;
import team.fjut.cf.pojo.po.SpiderSimRecord;
import team.fjut.cf.service.SpiderSimRecordService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/5/2]
 */
@Service
public class SpiderSimRecordServiceImpl implements SpiderSimRecordService {
    @Resource
    SpiderSimRecordMapper spiderSimRecordMapper;

    @Override
    public int insert(SpiderSimRecord spiderSimRecord) {
        return spiderSimRecordMapper.insertSelective(spiderSimRecord);
    }

    @Override
    public int update(SpiderSimRecord spiderSimRecord) {
        return spiderSimRecordMapper.updateByPrimaryKey(spiderSimRecord);
    }

    @Override
    public List<SpiderSimRecord> selectByGetProblemId(Integer spiderGetProblemId) {
        Example example = new Example(SpiderSimRecord.class);
        example.createCriteria().andEqualTo("spiderGetProblemId", spiderGetProblemId);
        return spiderSimRecordMapper.selectByExample(example);
    }
}
