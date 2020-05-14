package team.fjut.cf.controller.admin;

import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.SpiderSetting;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.SpiderSettingService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/5/14]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/spider/setting")
public class SpiderSettingManagerController {
    @Resource
    SpiderSettingService spiderSettingService;

    @PermissionRequired(permissions = {PermissionType.SPIDER_SETTING})
    @PostMapping("/list")
    public ResultJson getList() {
        List<SpiderSetting> spiderSettings = spiderSettingService.selectAll();
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderSettings);
    }

    @PermissionRequired(permissions = {PermissionType.SPIDER_SETTING})
    @PostMapping("/save")
    public ResultJson saveSetting(@RequestBody SpiderSetting spiderSetting) {
        int i = spiderSettingService.updateById(spiderSetting);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, i == 1);
    }
}
