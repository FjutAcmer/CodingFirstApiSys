package team.fjut.cf.pojo.vo.response;

import lombok.Data;

/**
 * 统计提交数与ac数
 * @author zhongml [2020/5/13]
 */
@Data
public class AcAndSubmitVO {
    private Integer[] contestAc;
    private Integer[] contestSubmit;
    private Integer[] totalAc;
    private Integer[] totalSubmit;
}
