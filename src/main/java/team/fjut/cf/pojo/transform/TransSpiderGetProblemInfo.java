package team.fjut.cf.pojo.transform;

import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
public class TransSpiderGetProblemInfo {
    public static List<SpiderProblemListVO> transformToListVO(List<SpiderGetProblemInfo> lists) {
        List<SpiderProblemListVO> results = new ArrayList<>();
        for (SpiderGetProblemInfo item : lists) {
            SpiderProblemListVO temp = transformToListVO(item);
            results.add(temp);
        }
        return results;
    }

    public static SpiderProblemListVO transformToListVO(SpiderGetProblemInfo item) {
        SpiderProblemListVO temp = new SpiderProblemListVO();
        temp.setId(item.getId());
        temp.setFromWebsite(item.getFromWebsite());
        temp.setInsertTime(item.getInsertTime());
        temp.setSpiderName(item.getSpiderName());
        temp.setSpiderJob(item.getSpiderJob());
        temp.setProblemUrl(item.getProblemUrl());
        temp.setProblemTitle(item.getProblemTitle());
        temp.setProblemId(item.getProblemId());
        temp.setIsLocalized(null);
        return temp;
    }
}
