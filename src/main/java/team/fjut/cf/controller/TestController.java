package team.fjut.cf.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.fjut.cf.component.spider.SpiderExecClient;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.vo.ResultJson;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author axiang [2019/10/21]
 */
@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {


    @Resource
    SpiderExecClient spiderExecClient;

    @GetMapping("/test")
    public ResultJson testMethod() throws IOException {
        String s = spiderExecClient.executeRedeploy();
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, s);
    }


}
