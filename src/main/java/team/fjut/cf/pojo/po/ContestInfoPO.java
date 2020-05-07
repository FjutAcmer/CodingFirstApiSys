package team.fjut.cf.pojo.po;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author axiang [2019/10/18]
 */
@Table(name = "t_contest_info")
public class ContestInfoPO {
    private Integer id;
    private Integer contestId;
    private Integer contestKind;
    private String title;
    private String description;
    private String createUser;
    private Date beginTime;
    private Date endTime;
    private Integer permissionType;
    private String password;
    private Date registerBeginTime;
    private Date registerEndTime;
    private Integer computerRating;
    private Integer rankType;
    private Integer problemPutTag;
    private Integer statusReadOut;
    private Integer showRegisterList;
    private Integer showBorderList;
    private Integer showOtherStatus;

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

    public Integer getContestKind() {
        return contestKind;
    }

    public void setContestKind(Integer contestKind) {
        this.contestKind = contestKind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterBeginTime() {
        return registerBeginTime;
    }

    public void setRegisterBeginTime(Date registerBeginTime) {
        this.registerBeginTime = registerBeginTime;
    }

    public Date getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(Date registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public Integer getComputerRating() {
        return computerRating;
    }

    public void setComputerRating(Integer computerRating) {
        this.computerRating = computerRating;
    }

    public Integer getRankType() {
        return rankType;
    }

    public void setRankType(Integer rankType) {
        this.rankType = rankType;
    }

    public Integer getProblemPutTag() {
        return problemPutTag;
    }

    public void setProblemPutTag(Integer problemPutTag) {
        this.problemPutTag = problemPutTag;
    }

    public Integer getStatusReadOut() {
        return statusReadOut;
    }

    public void setStatusReadOut(Integer statusReadOut) {
        this.statusReadOut = statusReadOut;
    }

    public Integer getShowRegisterList() {
        return showRegisterList;
    }

    public void setShowRegisterList(Integer showRegisterList) {
        this.showRegisterList = showRegisterList;
    }

    public Integer getShowBorderList() {
        return showBorderList;
    }

    public void setShowBorderList(Integer showBorderList) {
        this.showBorderList = showBorderList;
    }

    public Integer getShowOtherStatus() {
        return showOtherStatus;
    }

    public void setShowOtherStatus(Integer showOtherStatus) {
        this.showOtherStatus = showOtherStatus;
    }

    @Override
    public String toString() {
        return "ContestInfoPO{" +
                "id=" + id +
                ", contestId=" + contestId +
                ", contestKind=" + contestKind +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createUser='" + createUser + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", permissionType=" + permissionType +
                ", password='" + password + '\'' +
                ", registerBeginTime=" + registerBeginTime +
                ", registerEndTime=" + registerEndTime +
                ", computerRating=" + computerRating +
                ", rankType=" + rankType +
                ", problemPutTag=" + problemPutTag +
                ", statusReadOut=" + statusReadOut +
                ", showRegisterList=" + showRegisterList +
                ", showBorderList=" + showBorderList +
                ", showOtherStatus=" + showOtherStatus +
                '}';
    }
}
