package team.fjut.cf.component.spider;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 爬虫通讯类
 *
 * @author axiang [2020/4/28]
 */
@Component
@Slf4j
public class SpiderHttpClient {
    @Resource
    RestTemplate restTemplate;

    private static final String PROJECT_NAME = "CodingFirstSpider";

    @Value("${cf.config.spider.daemonStatusUrl}")
    private String daemonStatusUrl;

    @Value("${cf.config.spider.listProjectsUrl}")
    private String listProjectsUrl;

    @Value("${cf.config.spider.listSpidersUrl}")
    private String listSpidersUrl;

    @Value("${cf.config.spider.listJobsUrl}")
    private String listJobsUrl;

    @Value("${cf.config.spider.scheduleUrl}")
    private String scheduleUrl;

    @Value("${cf.config.spider.logUrl}")
    private String logUrl;

    /**
     * 初始化设置头部
     */
    public SpiderHttpClient() {
        //入参设置头部
    }

    /**
     * 获取scrapyd服务器运行状态
     *
     * @return
     */
    public JSONObject getDaemonStatus() {
        return restTemplate.getForObject(daemonStatusUrl, JSONObject.class);
    }

    /**
     * 获取scrapyd服务器已部署爬虫项目列表
     *
     * @return
     */
    public JSONObject getListProjects() {
        return restTemplate.getForObject(listProjectsUrl, JSONObject.class);
    }

    /**
     * 获取爬虫列表
     *
     * @return
     */
    public JSONObject getListSpiders() {
        String currentUrl = String.format(listSpidersUrl, PROJECT_NAME);
        return restTemplate.getForObject(currentUrl, JSONObject.class);
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    public JSONObject getListJobs() {
        String currentUrl = String.format(listJobsUrl, PROJECT_NAME);
        return restTemplate.getForObject(currentUrl, JSONObject.class);
    }

    /**
     * 立即启动爬虫
     *
     * @param spiderName 爬虫名
     * @return 启动结果
     */
    public JSONObject startSpider(String spiderName, String job, String problems) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("project", PROJECT_NAME);
        map.add("spider", spiderName);
        map.add("job", job);
        map.add("problems", problems);
        HttpEntity<MultiValueMap<String, Object>> request =
                new HttpEntity<>(map);
        return restTemplate.postForObject(scheduleUrl, request, JSONObject.class);
    }

    public String getSpiderLog(String spiderName, String jobId) {
        String currentUrl = String.format(logUrl, PROJECT_NAME, spiderName, jobId);
        return restTemplate.getForObject(currentUrl, String.class);
    }

}
