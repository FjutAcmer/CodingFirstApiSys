package team.fjut.cf.pojo.vo.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author axiang [2020/5/2]
 */
@Data
public class SpiderLocalizedRecordVo {
    Integer id;
    Integer spiderGetProblemId;
    Integer localProblemId;
    String createUser;
    String operateRecord;
    JSONObject simRecord;
}
