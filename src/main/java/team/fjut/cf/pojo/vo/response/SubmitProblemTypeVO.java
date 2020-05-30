package team.fjut.cf.pojo.vo.response;

import lombok.Data;

/**
 * @author zhongml [2020/5/13]
 */
@Data
public class SubmitProblemTypeVO {
    private Integer problemId;
    private Integer problemTypeId;
    private String problemTypeName;
    private Integer count;
}
