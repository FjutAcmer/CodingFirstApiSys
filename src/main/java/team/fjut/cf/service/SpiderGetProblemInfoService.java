package team.fjut.cf.service;

import team.fjut.cf.component.textsim.pojo.ProblemInfoToSim;
import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.vo.request.LocalizedProblemVO;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;

import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
public interface SpiderGetProblemInfoService {


    boolean localizedProblem(LocalizedProblemVO LocalizedProblem);

    List<SpiderProblemListVO> pages(int pageNum, int pageSize);

    int count();

    SpiderGetProblemInfo selectById(int id);

    ProblemInfoToSim selectToSimById(int id);
}
