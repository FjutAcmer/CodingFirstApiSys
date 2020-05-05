package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author axiang [2020/5/2]
 */
@Data
@Table(name = "t_spider_sim_record")
public class SpiderSimRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    Integer spiderGetProblemId;
    Integer localProblemId;
    String createUser;
    String operateRecord;
    String simRecord;
}
