package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.VjProblemInfoMapper;
import team.fjut.cf.pojo.po.VjProblemInfo;
import team.fjut.cf.service.VjProblemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author axiang [2020/3/13]
 */
@Service
public class VjProblemInfoServiceImpl implements VjProblemInfoService {
    @Resource
    VjProblemInfoMapper vjProblemInfoMapper;

    @Override
    public int insert(VjProblemInfo vjProblemInfo) {
        return vjProblemInfoMapper.insertSelective(vjProblemInfo);
    }

    @Override
    public int update(VjProblemInfo vjProblemInfo) {
        return vjProblemInfoMapper.updateByPrimaryKeySelective(vjProblemInfo);
    }

    @Override
    public VjProblemInfo select(String OJId, String probNum) {
        Example example = new Example(VjProblemInfo.class);
        example.createCriteria().andEqualTo("ojId", OJId)
                .andEqualTo("probNum", probNum);
        return vjProblemInfoMapper.selectOneByExample(example);
    }
}
