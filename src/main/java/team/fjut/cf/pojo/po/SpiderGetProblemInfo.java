package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author axiang [2020/4/29]
 */
@Data
@Table(name = "t_spider_get_problem_info")
public class SpiderGetProblemInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    String spiderName;
    String spiderJob;
    Date insertTime;
    String fromWebsite;
    String problemUrl;
    String problemId;
    String problemTitle;
    String problemTimeLimit;
    String problemMemoryLimit;
    String problemDescription;
    String problemInput;
    String problemOutput;
    String problemSampleInput;
    String problemSampleOutput;

}
