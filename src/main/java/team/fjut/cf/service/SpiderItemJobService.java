package team.fjut.cf.service;

import team.fjut.cf.pojo.po.SpiderItemJob;

/**
 * @author axiang
 */
public interface SpiderItemJobService {
    int insert(SpiderItemJob spiderItemJob);

    void updateByJobId(String jobId, SpiderItemJob spiderItemJob);

    int updateById(Integer id, SpiderItemJob spiderItemJob);
}
