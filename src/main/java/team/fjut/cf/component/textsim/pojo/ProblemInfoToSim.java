package team.fjut.cf.component.textsim.pojo;

import lombok.Data;

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
        return title + "\n" + description + "\n" + input + "\n" + output;
    }
}
