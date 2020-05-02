package team.fjut.cf.pojo.transform;

import com.alibaba.fastjson.JSONObject;
import team.fjut.cf.pojo.po.SpiderLocalizedRecord;
import team.fjut.cf.pojo.vo.response.SpiderLocalizedRecordVo;

/**
 * @author axiang [2020/5/2]
 */
public class TransSpiderLocalizedRecord {
    public static SpiderLocalizedRecordVo transformToVo(SpiderLocalizedRecord spiderLocalizedRecord) {
        SpiderLocalizedRecordVo result = new SpiderLocalizedRecordVo();
        result.setId(spiderLocalizedRecord.getId());
        result.setCreateUser(spiderLocalizedRecord.getCreateUser());
        result.setLocalProblemId(spiderLocalizedRecord.getLocalProblemId());
        result.setSpiderGetProblemId(spiderLocalizedRecord.getSpiderGetProblemId());
        result.setSimRecord(JSONObject.parseObject(spiderLocalizedRecord.getSimRecord()));
        result.setOperateRecord(spiderLocalizedRecord.getOperateRecord());
        return result;
    }
}
