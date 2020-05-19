package team.fjut.cf.pojo.transform;

import com.alibaba.fastjson.JSONObject;
import team.fjut.cf.pojo.po.SpiderSimRecord;
import team.fjut.cf.pojo.vo.response.SpiderLocalizedRecordVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author axiang [2020/5/2]
 */
public class TransSpiderLocalizedRecord {
    public static List<SpiderLocalizedRecordVo> transformToVo(List<SpiderSimRecord> items) {
        List<SpiderLocalizedRecordVo> results = new ArrayList<>();
        for (SpiderSimRecord item : items) {
            results.add(transformToVo(item));
        }
        return results;
    }

    public static SpiderLocalizedRecordVo transformToVo(SpiderSimRecord spiderSimRecord) {
        SpiderLocalizedRecordVo result = new SpiderLocalizedRecordVo();
        result.setId(spiderSimRecord.getId());
        result.setCreateUser(spiderSimRecord.getCreateUser());
        result.setLocalProblemId(spiderSimRecord.getLocalProblemId());
        result.setSpiderGetProblemId(spiderSimRecord.getSpiderGetProblemId());
        result.setSimRecord(JSONObject.parseObject(spiderSimRecord.getSimRecord()));
        result.setOperateRecord(spiderSimRecord.getOperateRecord());
        return result;
    }
}
