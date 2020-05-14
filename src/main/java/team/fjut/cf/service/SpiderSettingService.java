package team.fjut.cf.service;

import team.fjut.cf.pojo.po.SpiderSetting;

import java.util.List;

public interface SpiderSettingService {
    List<SpiderSetting> selectAll();

    int updateById(SpiderSetting spiderSetting);

    int updateByKey(String key, SpiderSetting spiderSetting);
}
