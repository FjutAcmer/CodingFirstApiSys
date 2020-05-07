package team.fjut.cf.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author axiang [2019/11/18]
 */
@Data
public class ContestListVO {
    private Integer id;
    private String kind;
    private String title;
    private Date beginTime;
    private Date endTime;
    private String createUser;
    private String permission;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ContestListVO{" +
                "id=" + id +
                ", kind='" + kind + '\'' +
                ", title='" + title + '\'' +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", permission='" + permission + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
