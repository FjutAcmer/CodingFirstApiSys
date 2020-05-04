package team.fjut.cf.service;

import team.fjut.cf.pojo.po.SpiderLocalizedRecord;

import java.util.List;

/**
 * @author axiang [2020/5/2]
 */
public interface SpiderLocalizedRecordService {
    int insert(SpiderLocalizedRecord spiderLocalizedRecord);

    int update(SpiderLocalizedRecord spiderLocalizedRecord);

    List<SpiderLocalizedRecord> selectByGetProblemId(Integer getProblemId);
}
