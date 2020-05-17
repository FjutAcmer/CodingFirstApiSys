package team.fjut.cf.pojo.vo.response;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhongml [2020/5/14]
 */
@Data
public class BugReportVO {
    Integer id;
    String username;
    String title;
    String currentPath;
    String type;
    String text;
    Date reportTime;
    Integer isFixed;
}
