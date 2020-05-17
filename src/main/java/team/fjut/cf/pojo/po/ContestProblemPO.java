package team.fjut.cf.pojo.po;

import javax.persistence.Table;

/**
 * @author axiang [2019/10/18]
 */
@Table(name = "t_contest_problem")
public class ContestProblemPO {
    private Integer id;
    private Integer contestId;
    private Integer problemId;
    private Integer problemOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    public Integer getProblemId() {
        return problemId;
    }

    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }

    public Integer getProblemOrder() {
        return problemOrder;
    }

    public void setProblemOrder(Integer problemOrder) {
        this.problemOrder = problemOrder;
    }

    @Override
    public String toString() {
        return "ContestProblemPO{" +
                "id=" + id +
                ", contestId=" + contestId +
                ", problemId=" + problemId +
                ", problemOrder=" + problemOrder +
                '}';
    }
}
