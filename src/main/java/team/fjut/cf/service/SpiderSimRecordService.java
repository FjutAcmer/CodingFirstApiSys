package team.fjut.cf.service;

import team.fjut.cf.pojo.po.SpiderSimRecord;

import java.util.List;

/**
 * @author axiang [2020/5/2]
 */
public interface SpiderSimRecordService {
    int insert(SpiderSimRecord spiderSimRecord);

    int update(SpiderSimRecord spiderSimRecord);

    List<SpiderSimRecord> selectByGetProblemId(Integer getProblemId);
}
