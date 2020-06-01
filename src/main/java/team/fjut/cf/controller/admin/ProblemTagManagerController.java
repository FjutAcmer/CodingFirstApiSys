package team.fjut.cf.controller.admin;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.po.ProblemTagPO;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.service.ProblemTagService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongml [2020/4/20]
 */
@RestController
@RequestMapping("/admin/tag")
@CrossOrigin
public class ProblemTagManagerController {
    @Resource
    ProblemTagService problemTagService;

    /**
     * @param page
     * @param limit
     * @param sort
     * @param name
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_TAG_MANAGER})
    @GetMapping("/list")
    public ResultJson getTagLimit(@RequestParam("page") Integer page,
                                  @RequestParam("limit") Integer limit,
                                  @RequestParam(value = "sort", required = false) String sort,
                                  @RequestParam(value = "name", required = false) String name) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        if (page == null) {
            page = 1;
        }
        if (limit == null || limit > 100) {
            limit = 20;
        }
        if (!StringUtils.isEmpty(name)) {
            // 拼接查询字符串
            name = "%" + name + "%";
        } else {
            // 拼接查询字符串如果为空字符或者null则 置为null
            name = null;
        }
        List<ProblemTagPO> problemTag = problemTagService.selectByCondition(page, limit, sort, name);
        int total = problemTagService.countByCondition(name);
        resultJson.addInfo(problemTag);
        resultJson.addInfo(total);
        return resultJson;
    }

    /**
     * @param id
     * @param name
     * @param tagType
     * @param priority
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_TAG_MANAGER})
    @PutMapping("/update")
    public ResultJson updateTag(@RequestParam("id") Integer id,
                                @RequestParam("name") String name,
                                @RequestParam("tagType") Integer tagType,
                                @RequestParam("priority") Integer priority) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ProblemTagPO problemTag = new ProblemTagPO();
        problemTag.setId(id);
        problemTag.setName(name);
        problemTag.setTagType(tagType);
        problemTag.setPriority(priority);
        int result1 = problemTagService.updateTag(problemTag);
        if (result1 != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * @param name
     * @param tagType
     * @param createUser
     * @param priority
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_TAG_MANAGER})
    @PostMapping("/create")
    public ResultJson createTag(@RequestParam("name") String name,
                                @RequestParam("tagType") Integer tagType,
                                @RequestParam("createUser") String createUser,
                                @RequestParam("priority") Integer priority) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        ProblemTagPO problemTag = new ProblemTagPO();
        problemTag.setName(name);
        problemTag.setTagType(tagType);
        problemTag.setCreateUser(createUser);
        problemTag.setPriority(priority);
        int result1 = problemTagService.createTag(problemTag);
        if (result1 != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * @param id
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.PROBLEM_TAG_MANAGER})
    @DeleteMapping("/delete")
    public ResultJson deleteTag(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result1 = problemTagService.deleteTag(id);
        System.out.println(result1);
        if (result1 != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

}
