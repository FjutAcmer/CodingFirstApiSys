package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.VjUserProblemSolvedMapper;
import team.fjut.cf.pojo.po.VjJudgeResult;
import team.fjut.cf.pojo.po.VjUserProblemSolved;
import team.fjut.cf.service.VjUserProblemSolvedService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author axiang [2020/3/24]
 */
@Service
public class VjUserProblemSolvedServiceImpl implements VjUserProblemSolvedService {
    @Resource
    VjUserProblemSolvedMapper vjUserProblemSolvedMapper;

    @Override
    public int insert(VjUserProblemSolved vjUserProblemSolved) {
        return vjUserProblemSolvedMapper.insertSelective(vjUserProblemSolved);
    }

    @Override
    public void ifSubmitSuccess(VjJudgeResult vjJudgeResult) {
        Example example = new Example(VjUserProblemSolved.class);
        example.createCriteria().andEqualTo("ojId", vjJudgeResult.getOj())
                .andEqualTo("probNum", vjJudgeResult.getProbNum())
                .andEqualTo("username", vjJudgeResult.getUsername());
        // 查询解答题目表中的记录
        VjUserProblemSolved vjUserProblemSolved = vjUserProblemSolvedMapper.selectOneByExample(example);
        // 如果解答表中没有先前的解答记录，代表是用户第一次答这题
        if (Objects.isNull(vjUserProblemSolved)) {
            // 插入一条新的解答表记录
            VjUserProblemSolved newRecord = new VjUserProblemSolved();
            newRecord.setUsername(vjJudgeResult.getUsername());
            newRecord.setProbNum(vjJudgeResult.getProbNum());
            newRecord.setOjId(vjJudgeResult.getOj());
            newRecord.setTryCount(1);
            newRecord.setSolvedCount(0);
            newRecord.setLastTryTime(vjJudgeResult.getSubmitTime());
            vjUserProblemSolvedMapper.insertSelective(newRecord);
        }
        // 用户已经答题过了
        else {
            vjUserProblemSolved.setTryCount(vjUserProblemSolved.getTryCount() + 1);
            vjUserProblemSolved.setLastTryTime(new Date());
            vjUserProblemSolvedMapper.updateByPrimaryKey(vjUserProblemSolved);
        }

    }

    @Override
    public VjUserProblemSolved selectOne(VjUserProblemSolved vjUserProblemSolved) {
        return vjUserProblemSolvedMapper.selectOne(vjUserProblemSolved);
    }
}
