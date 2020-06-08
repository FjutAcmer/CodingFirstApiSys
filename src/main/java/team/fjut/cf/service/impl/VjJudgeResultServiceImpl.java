package team.fjut.cf.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.fjut.cf.component.judge.vjudge.VirtualJudgeHttpClient;
import team.fjut.cf.component.judge.vjudge.pojo.ProblemSolution;
import team.fjut.cf.mapper.VjJudgeResultMapper;
import team.fjut.cf.mapper.VjUserProblemSolvedMapper;
import team.fjut.cf.pojo.po.VjJudgeResult;
import team.fjut.cf.pojo.po.VjUserProblemSolved;
import team.fjut.cf.service.VjJudgeResultService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author axiang [2020/3/21]
 */
@Service
public class VjJudgeResultServiceImpl implements VjJudgeResultService {
    @Resource
    VjJudgeResultMapper vjJudgeResultMapper;

    @Resource
    VjUserProblemSolvedMapper vjUserProblemSolvedMapper;

    @Resource
    VirtualJudgeHttpClient virtualJudgeHttpClient;

    @Override
    public int insert(VjJudgeResult vjJudgeResult) {
        return vjJudgeResultMapper.insertSelective(vjJudgeResult);
    }

    @Override
    public VjJudgeResult select(int id) {
        VjJudgeResult vjJudgeResult = new VjJudgeResult();
        vjJudgeResult.setId(id);
        return vjJudgeResultMapper.selectOne(vjJudgeResult);
    }

    @Async
    @Override
    public void queryResultFromVJ(VjJudgeResult vjJudgeResult) {
        boolean isProcessing;
        // 循环次数
        int times = 100;
        // 线程睡眠时间
        int sleepTime = 2000;
        ProblemSolution result;
        do {
            result = virtualJudgeHttpClient.getResult(vjJudgeResult.getRunId().toString());
            vjJudgeResult.setAdditionalInfo(result.getAdditionalInfo());
            vjJudgeResult.setStatus(result.getStatus());
            vjJudgeResult.setStatusType(result.getStatusType());
            vjJudgeResult.setStatusCanonical(result.getStatusCanonical());
            vjJudgeResult.setRuntime(result.getRuntime());
            vjJudgeResult.setMemory(result.getMemory());
            vjJudgeResult.setRemoteRunId(result.getRemoteRunId());
            vjJudgeResult.setProcessing(result.getProcessing() ? 1 : 0);
            vjJudgeResult.setCode(result.getCode());
            vjJudgeResult.setLength(result.getLength());
            vjJudgeResult.setLanguageCanonical(result.getLanguageCanonical());
            vjJudgeResult.setLanguage(result.getLanguage());
            vjJudgeResult.setAuthorId(result.getAuthorId());
            vjJudgeResult.setAuthor(result.getAuthor());
            vjJudgeResultMapper.updateByPrimaryKeySelective(vjJudgeResult);
            isProcessing = result.getProcessing();
            times--;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {

            }
        } while (times > 0 && isProcessing);
        /*
        完成结果获取后，更新题目解答表
         */
        Example example = new Example(VjUserProblemSolved.class);
        example.createCriteria().andEqualTo("ojId", vjJudgeResult.getOj())
                .andEqualTo("probNum", vjJudgeResult.getProbNum())
                .andEqualTo("username", vjJudgeResult.getUsername());
        // 查询解答题目表中的记录。由于提交时已经**插入**过了，所以表中一定有记录
        VjUserProblemSolved vjUserProblemSolved = vjUserProblemSolvedMapper.selectOneByExample(example);
        // 如果结果是AC
        if ("AC".equals(result.getStatusCanonical())) {
            // 第一次解答则更新第一次解答时间
            if (vjUserProblemSolved.getSolvedCount() == 0) {
                vjUserProblemSolved.setFirstSolvedTime(vjJudgeResult.getSubmitTime());
                vjUserProblemSolved.setSolvedCount(1);
            }
            // 前面已经解决过了，再一次解决
            else {
                vjUserProblemSolved.setSolvedCount(vjUserProblemSolved.getSolvedCount() + 1);
            }
        }
        vjUserProblemSolvedMapper.updateByPrimaryKey(vjUserProblemSolved);
    }


}
