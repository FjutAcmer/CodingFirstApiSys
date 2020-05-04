package team.fjut.cf.pojo.vo.request;

import lombok.Data;

/**
 * @author axiang [2020/5/3]
 */
@Data
public class LocalizedProblemVO {
    String problemId;
    String problemTitle;
    String problemTimeLimit;
    String problemMemoryLimit;
    String problemDescription;
    String problemInput;
    String problemOutput;
    String problemSampleInput;
    String problemSampleOutput;
    String problemHint;
    Integer spj;
    String intFormat;

}
