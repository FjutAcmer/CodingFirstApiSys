package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SpiderGetProblemInfoMapper;
import team.fjut.cf.pojo.po.SpiderGetProblemInfo;
import team.fjut.cf.pojo.transform.TransSpiderGetProblemInfo;
import team.fjut.cf.pojo.vo.response.SpiderProblemListVO;
import team.fjut.cf.service.SpiderGetProblemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
@Service
@Slf4j
public class SpiderGetProblemInfoServiceImpl implements SpiderGetProblemInfoService {
    @Resource
    SpiderGetProblemInfoMapper spiderGetProblemInfoMapper;

    @Override
    public List<SpiderProblemListVO> pages(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(SpiderGetProblemInfo.class);
        example.orderBy("id").desc();
        List<SpiderGetProblemInfo> spiderGetProblemInfos = spiderGetProblemInfoMapper.selectByExample(example);
        return TransSpiderGetProblemInfo.transformToListVO(spiderGetProblemInfos);
    }

    @Override
    public int count() {
        Example example = new Example(SpiderGetProblemInfo.class);
        return spiderGetProblemInfoMapper.selectCountByExample(example);
    }
}
