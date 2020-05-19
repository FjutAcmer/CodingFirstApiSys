package team.fjut.cf.pojo.vo.response;

import lombok.Data;

import java.util.Date;

/**
 * @author axiang [2020/4/30]
 */
@Data
public class SpiderProblemListVO {
    Integer id;
    String spiderName;
    String spiderJob;
    Date insertTime;
    String fromWebsite;
    String problemUrl;
    String problemId;
    String problemTitle;
    Boolean isLocalized;
}
