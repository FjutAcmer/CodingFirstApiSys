package team.fjut.cf.component.textsim.pojo;

import lombok.Data;
import team.fjut.cf.utils.HtmlTagUtils;

/**
 * 文本匹配类
 *
 * @author axiang [2020/5/2]
 */
@Data
public class ProblemInfoToSim {
    String title;
    String description;
    String input;
    String output;

    /**
     * 将属性中的标签去除，保留纯文本
     *
     * @return
     */
    public String toPureString() {
        return title + "\n" +
                HtmlTagUtils.delHTMLTag(description) + "\n"
                + HtmlTagUtils.delHTMLTag(input) + "\n"
                + HtmlTagUtils.delHTMLTag(output);
    }
}
