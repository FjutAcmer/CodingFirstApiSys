package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author axiang [2020/4/30]
 */
@Data
@Table(name = "t_spider_item_job")
public class SpiderItemJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    String jobId;
    String spiderName;
    Date planStartTime;
    Date planEndTime;
    Date actualStartTime;
    Date actualEndTime;
    String problemRange;
    Integer currentStatus;
    String createUser;
    Integer forceCancel;
    String result;
}
