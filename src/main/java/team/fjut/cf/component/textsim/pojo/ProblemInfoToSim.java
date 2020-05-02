package team.fjut.cf.component.textsim.pojo;

import lombok.Data;
import team.fjut.cf.utils.HtmlTagUtils;

/**
 * @author axiang [2020/5/2]
 */
@Data
public class ProblemInfoToSim {
    String title;
    String description;
    String input;
    String output;

    public String toPureString() {
        return title + "\n" +
                HtmlTagUtils.delHTMLTag(description) + "\n"
                + HtmlTagUtils.delHTMLTag(input) + "\n"
                + HtmlTagUtils.delHTMLTag(output);
    }
}
