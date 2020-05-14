package team.fjut.cf.pojo.vo.response;

import lombok.Data;

/**
 * 统计语言使用次数
 * @author zhongml [2020/5/13]
 */
@Data
public class LanguageUsedNumVO {
    private Integer language;
    private String languageName;
    private Integer usedNum;
}
