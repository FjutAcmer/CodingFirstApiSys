package team.fjut.cf.controller.admin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import team.fjut.cf.component.spider.SpiderHttpClient;
import team.fjut.cf.component.spider.SpiderParamsTool;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.SpiderItemInfo;
import team.fjut.cf.pojo.po.SpiderItemJob;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.request.QuerySpiderLogVO;
import team.fjut.cf.pojo.vo.request.StartSpiderVO;
import team.fjut.cf.pojo.vo.response.SpiderJobCountVO;
import team.fjut.cf.pojo.vo.response.SpiderJobListVO;
import team.fjut.cf.service.SpiderItemInfoService;
import team.fjut.cf.service.SpiderItemJobService;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author axiang [2020/4/28]
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/admin/spider")
public class SpiderManagerController {
    @Resource
    SpiderHttpClient spiderHttpClient;

    @Resource
    SpiderItemInfoService spiderItemInfoService;

    @Resource
    SpiderItemJobService spiderItemJobService;

    @PermissionRequired(permissions = {PermissionType.SPIDER_SERVER_MANAGER})
    @PostMapping("/status")
    public ResultJson getStatus() {
        JSONObject daemonStatus = spiderHttpClient.getDaemonStatus();
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, daemonStatus);
    }

    @PermissionRequired(permissions = {})
    @PostMapping("/items")
    public ResultJson getItems(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        List<SpiderItemInfo> pages = spiderItemInfoService.pages(pageNum, pageSize);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, pages);
    }

    @PermissionRequired(permissions = {})
    @PostMapping("/sites")
    public ResultJson getSites(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        List<SpiderItemInfo> pages = spiderItemInfoService.pages(pageNum, pageSize);
        Map<String, List<SpiderItemInfo>> map = new HashMap<>();
        for (SpiderItemInfo page : pages) {
            if (map.get(page.getTargetWebsiteName()) == null) {
                List<SpiderItemInfo> temp = new ArrayList<>();
                temp.add(page);
                map.put(page.getTargetWebsiteName(), temp);
            } else {
                List<SpiderItemInfo> spiderItemInfos = map.get(page.getTargetWebsiteName());
                spiderItemInfos.add(page);
                map.put(page.getTargetWebsiteName(), spiderItemInfos);
            }
        }
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, map);
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_RANGE_SCARY})
    @PostMapping("/start/spec")
    public ResultJson startSpecSpider(@RequestBody StartSpiderVO startSpiderVO) {
        if (startSpiderVO.getSpiderName().toLowerCase().contains("spec")) {
            String problems = SpiderParamsTool.parseRange2Problems(startSpiderVO.getRange());
            if (Objects.nonNull(problems) && problems.split(",").length >= 50) {
                return new ResultJson(ResultCode.BUSINESS_FAIL, "爬取范围过大！");
            }
            SpiderItemJob spiderItemJob = new SpiderItemJob();
            spiderItemJob.setActualStartTime(new Date());
            spiderItemJob.setCreateUser(startSpiderVO.getUsername());
            spiderItemJob.setForceCancel(0);
            spiderItemJob.setCurrentStatus(0);
            spiderItemJob.setSpiderName(startSpiderVO.getSpiderName());
            spiderItemJob.setProblemRange(startSpiderVO.getRange());
            spiderItemJobService.insert(spiderItemJob);
            JSONObject jsonObject = spiderHttpClient.startSpider(startSpiderVO.getSpiderName(),
                    spiderItemJob.getId().toString(),
                    problems);
            spiderItemJob.setJobId(jsonObject.getString("jobid"));
            spiderItemJobService.updateById(spiderItemJob.getId(), spiderItemJob);
            return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, jsonObject, spiderItemJob);
        } else {
            return new ResultJson(ResultCode.BUSINESS_FAIL, "不是范围爬虫！");
        }
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_ALL_SCARY})
    @PostMapping("/start/full")
    public ResultJson startFullSpider(@RequestBody StartSpiderVO startSpiderVO) {
        if (startSpiderVO.getSpiderName().toLowerCase().contains("full")) {
            String problems = SpiderParamsTool.parseRange2Problems(startSpiderVO.getRange());
            SpiderItemJob spiderItemJob = new SpiderItemJob();
            spiderItemJob.setActualStartTime(new Date());
            spiderItemJob.setCreateUser(startSpiderVO.getUsername());
            spiderItemJob.setForceCancel(0);
            spiderItemJob.setCurrentStatus(0);
            spiderItemJob.setSpiderName(startSpiderVO.getSpiderName());
            spiderItemJob.setProblemRange(startSpiderVO.getRange());
            spiderItemJobService.insert(spiderItemJob);
            JSONObject jsonObject = spiderHttpClient.startSpider(startSpiderVO.getSpiderName(),
                    spiderItemJob.getId().toString(),
                    problems);
            spiderItemJob.setJobId(jsonObject.getString("jobid"));
            spiderItemJobService.updateById(spiderItemJob.getId(), spiderItemJob);
            return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, jsonObject, spiderItemJob);
        } else {
            return new ResultJson(ResultCode.BUSINESS_FAIL, "不是全站爬虫！");
        }
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_RANGE_SCARY})
    @PostMapping("/range/check")
    public ResultJson rangeCheck(@RequestBody String range) {
        String problems = SpiderParamsTool.parseRange2Problems(range);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, problems);
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_ITEM_MANAGER})
    @PostMapping("/log/realtime")
    public ResultJson getJobLog(@RequestBody QuerySpiderLogVO querySpiderLogVO) {
        String spiderLog;
        try {
            spiderLog = spiderHttpClient.getSpiderLog(querySpiderLogVO.getSpiderName(), querySpiderLogVO.getJobId());
        } catch (HttpClientErrorException.NotFound e) {
            return new ResultJson(ResultCode.REQUIRED_SUCCESS, "", "【暂未找到日志！】");
        }
        SpiderItemJob spiderItemJob = new SpiderItemJob();
        spiderItemJob.setResult(spiderLog);
        spiderItemJobService.updateByJobId(querySpiderLogVO.getJobId(), spiderItemJob);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, "", spiderLog);
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_ITEM_MANAGER})
    @PostMapping("/job/info")
    public ResultJson getJobInfo(@RequestParam String jobId) {
        SpiderItemJob spiderItemJob = spiderItemJobService.selectByJobId(jobId);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderItemJob);
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_ITEM_MANAGER})
    @GetMapping("/job/list")
    public ResultJson getJobList(@RequestParam String spiderName) {
        List<SpiderJobListVO> spiderJobListVos = spiderItemJobService.selectBySpiderName(spiderName);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderJobListVos);
    }

    @PermissionRequired(permissions = {})
    @PostMapping("/count/days")
    public ResultJson getCountDays(@RequestParam int days) {
        List<SpiderJobCountVO> spiderJobCountVOS = spiderItemJobService.selectByCountDays(days);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderJobCountVOS);
    }

}
