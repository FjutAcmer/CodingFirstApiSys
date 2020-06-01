package team.fjut.cf.controller.admin;

import org.springframework.web.bind.annotation.*;
import team.fjut.cf.config.interceptor.annotation.PermissionRequired;
import team.fjut.cf.pojo.enums.PermissionType;
import team.fjut.cf.pojo.enums.ResultCode;
import team.fjut.cf.pojo.vo.ResultJson;
import team.fjut.cf.pojo.vo.response.BorderHonorRankVO;
import team.fjut.cf.service.BorderHonorRankService;

import javax.annotation.Resource;

/**
 * @author axiang [2019/11/11]
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/border")
public class BorderManagerController {
    @Resource
    BorderHonorRankService borderHonorRankService;

    /**
     * @param id
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.RANK_MANAGER})
    @GetMapping("/info")
    public ResultJson getHonor(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson();
        BorderHonorRankVO borderHonorRankVO = borderHonorRankService.selectById(id);
        resultJson.addInfo(borderHonorRankVO);
        return resultJson;
    }

    /**
     * @param id
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.RANK_MANAGER})
    @DeleteMapping("/delete")
    public ResultJson deleteHonor(@RequestParam("id") Integer id) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = borderHonorRankService.deleteHonor(id);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * @param borderHonorRankVO
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.RANK_MANAGER})
    @RequestMapping("/update")
    public ResultJson updateHonor(@RequestBody BorderHonorRankVO borderHonorRankVO) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = borderHonorRankService.updateHonor(borderHonorRankVO);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }

    /**
     * @param borderHonorRankVO
     * @return
     */
    @PermissionRequired(permissions = {PermissionType.RANK_MANAGER})
    @RequestMapping("/create")
    public ResultJson createHonor(@RequestBody BorderHonorRankVO borderHonorRankVO) {
        ResultJson resultJson = new ResultJson(ResultCode.REQUIRED_SUCCESS);
        int result = borderHonorRankService.insertHonor(borderHonorRankVO);
        if (result != 1) {
            resultJson.setStatus(ResultCode.BUSINESS_FAIL);
        }
        return resultJson;
    }


}
