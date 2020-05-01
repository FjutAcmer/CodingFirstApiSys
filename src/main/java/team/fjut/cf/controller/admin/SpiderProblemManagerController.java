package team.fjut.cf.controller.admin;

import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;
import team.fjut.cf.service.SpiderGetProblemInfoService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/spider/problem")
public class SpiderProblemManagerController {
    @Resource
    SpiderGetProblemInfoService spiderGetProblemInfoService;

    @GetMapping("/list")
    public ResultJson getSpiderProblemList(@RequestParam int page,
                                           @RequestParam int limit) {
        List<SpiderProblemListVO> pages = spiderGetProblemInfoService.pages(page, limit);
        int count = spiderGetProblemInfoService.count();
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, pages, count);
    }

    @GetMapping("/info")
    public ResultJson getSpiderProblemInfo(@RequestParam int id) {
        SpiderGetProblemInfo spiderGetProblemInfo = spiderGetProblemInfoService.selectById(id);
        return new ResultJson(ResultCode.REQUIRED_SUCCESS, null, spiderGetProblemInfo);
    }
}
