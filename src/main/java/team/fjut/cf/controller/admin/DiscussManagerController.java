package team.fjut.cf.controller.admin;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.DiscussPostPO;
import team.fjut.cf.pojo.po.DiscussReplyPostPO;
import team.fjut.cf.pojo.vo.DiscussPostVO;
import team.fjut.cf.pojo.vo.DiscussReplyPostVO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.DiscussPostService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongml [2020/4/27]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/discuss")
public class DiscussManagerController {
    @Resource
    DiscussPostService discussPostService;


    @GetMapping("/post/list")
    public ResultJson getPostList(@RequestParam("pageNum") Integer pageNum,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam(name = "sort", required = false) String sort,
                                  @RequestParam(name = "title", required = false) String title,
                                  @RequestParam(name = "author", required = false) String author) {
        ResultJson resultJson = new ResultJson();
        if (StringUtils.isEmpty(title)) {
            title = null;
        } else {
            title = "%" + title + "%";
        }
        if (StringUtils.isEmpty(author)) {
            author = null;
        } else {
            author = "%" + author + "%";
        }
        List<DiscussPostVO> discussPostVOS = discussPostService.selectByCondition(pageNum, pageSize, sort, title, author);
        Integer count = discussPostService.countByCondition(title, author);
        resultJson.addInfo(discussPostVOS);
        resultJson.addInfo(count);
        return resultJson;
    }

    @GetMapping("/reply/list")
    public ResultJson getPostList(@RequestParam("page") Integer pageNum,
                                  @RequestParam("limit") Integer pageSize,
                                  @RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson();
        List<DiscussReplyPostVO> discussReplyPostVOS = discussPostService.selectByPage(pageNum, pageSize, id);
        Integer count = discussPostService.countReplyById(id);
        resultJson.addInfo(discussReplyPostVOS);
        resultJson.addInfo(count);
        return resultJson;
    }

    @DeleteMapping("/post/delete")
    public ResultJson deletePost(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = discussPostService.deletePost(id);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "删除失败");
        }
        return resultJson;
    }

    @DeleteMapping("/reply/delete")
    public ResultJson deleteReply(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = discussPostService.deleteReplyPost(id);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL, "删除失败");
        }
        return resultJson;
    }
}
