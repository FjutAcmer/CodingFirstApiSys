package team.fjut.cf.service;

import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;

import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
public interface SpiderGetProblemInfoService {

    List<SpiderProblemListVO> pages(int pageNum, int pageSize);

    int count();
}