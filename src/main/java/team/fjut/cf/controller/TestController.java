package team.fjut.cf.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.fjut.cf.component.judge.vjudge.VirtualJudgeHttpClient;
import team.fjut.cf.component.spider.SpiderHttpClient;
import team.fjut.cf.component.textsim.TextSimExecuteClient;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.utils.JsonFileTool;

import javax.annotation.Resource;

/**
 * @author axiang [2019/10/21]
 */
@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {
    @Resource
    VirtualJudgeHttpClient virtualJudgeHttpClient;

    @Resource
    JsonFileTool jsonFileTool;

    @Resource
    SpiderHttpClient spiderHttpClient;

    @Resource
    TextSimExecuteClient textSimExecuteClient;

    @GetMapping("/test")
    public ResultJson testMethod() {
        JSONObject jsonObject = textSimExecuteClient.executeTextSim(
                "D://testfile/test1.txt",
                "D://testfile/sample.txt",
                "D://testfile/result-test1.json");
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, jsonObject);
    }


}
