package team.fjut.cf.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.fjut.cf.component.judge.vjudge.VirtualJudgeHttpClient;
import team.fjut.cf.component.spider.SpiderHttpClient;
import team.fjut.cf.component.textsim.TextSimExecuteClient;
import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.utils.JsonFileTool;

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
    VirtualJudgeHttpClient virtualJudgeHttpClient;

    @Resource
    JsonFileTool jsonFileTool;

    @Resource
    SpiderHttpClient spiderHttpClient;

    @Resource
    TextSimExecuteClient textSimExecuteClient;

    @GetMapping("/test")
    public ResultJson testMethod() throws IOException {
        ProblemInfoToSim problemInfoToSim1 = new ProblemInfoToSim();
        problemInfoToSim1.setTitle("魔法的题目");
        problemInfoToSim1.setDescription("<div>这是一道魔法的题目，我们需要这么做</div>");
        problemInfoToSim1.setInput("输入两个数字，我也不知道会发生什么");
        problemInfoToSim1.setOutput("真的不知道了啦");
        ProblemInfoToSim problemInfoToSim2 = new ProblemInfoToSim();
        problemInfoToSim2.setTitle("不够魔法的题目");
        problemInfoToSim2.setDescription("<div>这是一道不魔法的题目，你就看着办吧我也不知道要怎么搞</div>");
        problemInfoToSim2.setInput("输入两个数字，我知道会发生什么");
        problemInfoToSim2.setOutput("真的是知道了啦");
        JSONObject jsonObject = textSimExecuteClient.SimTwoProblem("123", problemInfoToSim1, problemInfoToSim2);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, jsonObject);
    }


}
