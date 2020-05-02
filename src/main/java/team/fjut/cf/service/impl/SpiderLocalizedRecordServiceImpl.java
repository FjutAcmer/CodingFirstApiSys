package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SpiderLocalizedRecordMapper;
import team.fjut.cf.pojo.po.SpiderLocalizedRecord;
import team.fjut.cf.service.SpiderLocalizedRecordService;

import javax.annotation.Resource;

/**
 * @author axiang [2020/5/2]
 */
@Service
public class SpiderLocalizedRecordServiceImpl implements SpiderLocalizedRecordService {
    @Resource
    SpiderLocalizedRecordMapper spiderLocalizedRecordMapper;

    @Override
    public int insert(SpiderLocalizedRecord spiderLocalizedRecord) {
        return spiderLocalizedRecordMapper.insertSelective(spiderLocalizedRecord);
    }

    @Override
    public int update(SpiderLocalizedRecord spiderLocalizedRecord) {
        return spiderLocalizedRecordMapper.updateByPrimaryKey(spiderLocalizedRecord);
    }
}
