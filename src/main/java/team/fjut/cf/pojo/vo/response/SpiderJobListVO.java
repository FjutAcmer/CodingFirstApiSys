package team.fjut.cf.pojo.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author axiang [2020/4/30]
 */
@Data
public class SpiderJobListVO {
    Integer id;
    String jobId;
    String spiderName;
    Date planStartTime;
    Date planEndTime;
    Date actualStartTime;
    Date actualEndTime;
    Integer currentStatus;
    String createUser;
    Integer forceCancel;

}
