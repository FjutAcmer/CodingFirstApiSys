package team.fjut.cf.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SpiderSettingMapper;
import team.fjut.cf.pojo.po.SpiderSetting;
import team.fjut.cf.service.SpiderSettingService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/5/14]
 */
@Service
@Slf4j
public class SpiderSettingServiceImpl implements SpiderSettingService {
    @Resource
    SpiderSettingMapper spiderSettingMapper;

    @Override
    public List<SpiderSetting> selectAll() {
        return spiderSettingMapper.selectAll();
    }

    @Override
    public int updateById(SpiderSetting spiderSetting) {
        return spiderSettingMapper.updateByPrimaryKey(spiderSetting);

    }

    @Override
    public int updateByKey(String key, SpiderSetting spiderSetting) {
        Example example = new Example(SpiderSetting.class);
        example.createCriteria().andEqualTo("key", key);
        return spiderSettingMapper.updateByExample(spiderSetting, example);
    }
}
