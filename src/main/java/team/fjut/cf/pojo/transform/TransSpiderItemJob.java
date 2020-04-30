package team.fjut.cf.pojo.transform;

import team.fjut.cf.pojo.po.SpiderItemJob;
import team.fjut.cf.pojo.vo.response.SpiderJobListVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
public class TransSpiderItemJob {
    public static List<SpiderJobListVO> transformToListVO(List<SpiderItemJob> jobs) {
        List<SpiderJobListVO> results = new ArrayList<>();
        for (SpiderItemJob job : jobs) {
            SpiderJobListVO temp = new SpiderJobListVO();
            temp.setActualEndTime(job.getActualEndTime());
            temp.setSpiderName(job.getSpiderName());
            temp.setPlanStartTime(job.getPlanStartTime());
            temp.setPlanEndTime(job.getPlanEndTime());
            temp.setJobId(job.getJobId());
            temp.setId(job.getId());
            temp.setForceCancel(job.getForceCancel());
            temp.setCurrentStatus(job.getCurrentStatus());
            temp.setCreateUser(job.getCreateUser());
            temp.setActualStartTime(job.getActualStartTime());
            results.add(temp);
        }
        return results;
    }
}
