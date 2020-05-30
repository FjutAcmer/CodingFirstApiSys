package team.fjut.cf.pojo.vo.response;

import lombok.Data;

/**
 * @author zhongml [2020/05/17]
 */
@Data
public class ContestProblemVO {
    private Integer contestId;
    private Integer problemId;
    private Integer problemOrder;
    private String title;
}
