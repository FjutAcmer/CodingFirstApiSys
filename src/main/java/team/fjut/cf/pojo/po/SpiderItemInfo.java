package team.fjut.cf.pojo.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author axiang [2020/4/29]
 */
@Data
@Table(name = "t_spider_item_info")
public class SpiderItemInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "JDBC")
    Integer id;
    String spiderName;
    String targetWebsiteUrl;
    String targetWebsiteLogoUrl;
    String targetWebsiteName;
    Integer spiderType;
    String antiMeasures;
    Integer deployStatus;
}
