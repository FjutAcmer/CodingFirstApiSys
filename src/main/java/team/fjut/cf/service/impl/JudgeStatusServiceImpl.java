package team.fjut.cf.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.fjut.cf.component.judge.local.LocalJudgeHttpClient;
import team.fjut.cf.mapper.*;
import team.fjut.cf.pojo.enums.CodeLanguage;
import team.fjut.cf.pojo.enums.SubmitResult;
import team.fjut.cf.pojo.po.*;
import team.fjut.cf.pojo.vo.JudgeStatusVO;
import team.fjut.cf.pojo.vo.StatusCountVO;
import team.fjut.cf.pojo.vo.response.LanguageUsedNumVO;
import team.fjut.cf.service.JudgeStatusService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2019/10/30]
 */
@Service
public class JudgeStatusServiceImpl implements JudgeStatusService {
    @Resource
    UserCustomInfoMapper userCustomInfoMapper;

    @Resource
    ProblemInfoMapper problemInfoMapper;

    @Resource
    JudgeStatusMapper judgeStatusMapper;

    @Resource
    JudgeResultMapper judgeResultMapper;

    @Resource
    UserProblemSolvedMapper userProblemSolvedMapper;

    @Resource
    UserBaseInfoMapper userBaseInfoMapper;

    @Resource
    LocalJudgeHttpClient localJudgeHttpClient;

    @Override
    public Integer insert(JudgeStatus judgeStatus) {
        return judgeStatusMapper.insertSelective(judgeStatus);
    }

    /**
     * 如果提交成功进行的操作如下
     * 首先对题目信息表进行操作，提交成功则 +1 提交数
     * 如果是用户第一次提交则 +1 提交用户数
     * 插入一条到解题记录表中
     *
     * @param judgeStatus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean ifSubmitSuccess(JudgeStatus judgeStatus) {
        String username = judgeStatus.getUsername();
        Integer problemId = judgeStatus.getProblemId();
        // 创建 题目信息表的检索条件
        Example exampleProbInfo = new Example(ProblemInfo.class);
        exampleProbInfo.createCriteria().andEqualTo("problemId", problemId);
        // 取得了problemId题目的数据
        ProblemInfo problemInfo = problemInfoMapper.selectOneByExample(exampleProbInfo);
        //题目ID的题目提交总数+1
        problemInfo.setTotalSubmit(problemInfo.getTotalSubmit() + 1);
        // 创建 题目解答表的检索条件
        Example exampleUserSolved = new Example(UserProblemSolved.class);
        exampleUserSolved.createCriteria().andEqualTo("username", username)
                .andEqualTo("problemId", problemId);
        // 查询解答题目表中的记录
        UserProblemSolved userProblemSolved = userProblemSolvedMapper.selectOneByExample(exampleUserSolved);
        // 如果解答表中没有先前的解答记录，代表是用户第一次答这题
        if (Objects.isNull(userProblemSolved)) {
            // 则题目ID的题目提交用户总数+1
            problemInfo.setTotalSubmitUser(problemInfo.getTotalSubmitUser() + 1);
            // 同时插入一条新的解答表记录
            UserProblemSolved newSolvedRecord = new UserProblemSolved();
            newSolvedRecord.setUsername(username);
            newSolvedRecord.setProblemId(problemId);
            newSolvedRecord.setTryCount(1);
            newSolvedRecord.setSolvedCount(0);
            newSolvedRecord.setLastTryTime(new Date());
            userProblemSolvedMapper.insertSelective(newSolvedRecord);
        } else {
            userProblemSolved.setTryCount(userProblemSolved.getTryCount() + 1);
            userProblemSolved.setLastTryTime(new Date());
            userProblemSolvedMapper.updateByExampleSelective(userProblemSolved, exampleUserSolved);

        }
        // 更新题目表
        problemInfoMapper.updateByExampleSelective(problemInfo, exampleProbInfo);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void ifLocalJudgeError(JudgeStatus judgeStatus) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        judgeStatus.setResult(SubmitResult.ERROR.getCode());
        // 更新评测记录中的评测结果为ERROR （提交失败）
        judgeStatusMapper.updateByPrimaryKeySelective(judgeStatus);
        // 更新评测结果内容
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setJudgeId(judgeStatus.getId());
        judgeResult.setInfo("本地评测机可能断开连接。失败于 " + dateFormat.format(new Date()));
        judgeResult.setTime(new Date());
        judgeResultMapper.insertSelective(judgeResult);
    }

    @Override
    public void ifSubmitError(JudgeStatus judgeStatus) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        judgeStatus.setResult(SubmitResult.ERROR.getCode());
        // 更新评测记录中的评测结果为ERROR （提交失败）
        judgeStatusMapper.updateByPrimaryKeySelective(judgeStatus);
        // 更新评测结果内容
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setJudgeId(judgeStatus.getId());
        judgeResult.setInfo("提交到本地评测机失败。失败于 " + dateFormat.format(new Date()));
        judgeResult.setTime(new Date());
        judgeResultMapper.insertSelective(judgeResult);
    }

    /**
     * 主要逻辑：
     * 首先从本地评测机拿数据
     * 如果评测机还在评测，更新评测结果表，并循环多次
     * 如果拿到了结果，交由handleLocalJudgeReturns方法处理
     * handleLocalJudgeReturns方法会把结果插入评测结果反馈表，并返回评测的真实结果
     * 如果始终拿不到结果，也更新用户解决表
     *
     * @param judgeStatus
     * @throws Exception
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void queryResultFromLocalJudge(JudgeStatus judgeStatus) throws Exception {
        int rid = judgeStatus.getId();
        JudgeStatus newJudgeStatus = new JudgeStatus();
        newJudgeStatus.setId(judgeStatus.getId());
        // 评测结果
        String judgingStr = "judging";
        // 退出循环标记
        boolean quitLoop = false;
        // 循环次数
        int times = 100;
        // 线程睡眠时间
        int sleepTime = 2000;
        do {
            // 从评测机中取得对应RunId的对应结果JSON
            JSONObject jsonObject = localJudgeHttpClient.getResultFromLocalJudge(rid);
            // 如果评测机返回状态为success，则继续
            if ("success".equals(jsonObject.getString("ret"))) {
                JSONObject resultJsonObj = JSONObject.parseObject(jsonObject.getString("result"));
                // 更新评测结果
                judgingStr = resultJsonObj.getString("type");
                // 如果代码在评测等待中，则不做操作
                if ("padding".equals(judgingStr)) {
                    newJudgeStatus.setResult(SubmitResult.PENDING.getCode());
                }
                // 如果代码在评测中，则不更新
                else if ("judging".equals(judgingStr)) {
                    newJudgeStatus.setResult(SubmitResult.JUDGING.getCode());
                    judgeStatusMapper.updateByPrimaryKeySelective(newJudgeStatus);
                }
                // 如果代码评测结果为编译失败，则更新状态
                else if ("CE".equals(judgingStr)) {
                    // 插入数据库表中评测结果为CE
                    JudgeResult judgeResult = new JudgeResult();
                    judgeResult.setJudgeId(judgeStatus.getId());
                    judgeResult.setInfo(resultJsonObj.getString("info"));
                    judgeResult.setTime(new Date());
                    judgeResultMapper.insertSelective(judgeResult);
                    newJudgeStatus.setResult(SubmitResult.CE.getCode());
                    judgeStatusMapper.updateByPrimaryKeySelective(newJudgeStatus);
                    // 退出循环
                    quitLoop = true;
                }
                //以下为编译正确返回的内容，即提交并且编译成功得到的结果，但不一定为AC
                else {
                    JSONArray retJsonArr = resultJsonObj.getJSONArray("ret");
                    // 评测机返回多组不同IO的评测记录，交由处理反馈结果
                    judgingStr = handleLocalJudgeReturns(retJsonArr, judgeStatus);
                    quitLoop = true;
                }
            } else {
                quitLoop = true;
            }
            times--;
            Thread.sleep(2000);
        } while (times > 0 && !quitLoop);
        // 200s的获取结果执行完毕或者拿到AC/CE编译的结果后执行
        String username = judgeStatus.getUsername();
        Integer problemId = judgeStatus.getProblemId();
        Example exampleProbInfo = new Example(ProblemInfo.class);
        exampleProbInfo.createCriteria().andEqualTo("problemId", problemId);
        ProblemInfo currentProbInfo = problemInfoMapper.selectOneByExample(exampleProbInfo);

        // 如果用户AC了这道题或者SCORE拿了100分
        if ("AC".equalsIgnoreCase(judgingStr) || "SC 100".equalsIgnoreCase(judgingStr)) {
            // 题目 AC 数量加一
            currentProbInfo.setTotalAc(currentProbInfo.getTotalAc() + 1);
            // 创建 题目解答表的检索条件
            Example exampleUserSolved = new Example(UserProblemSolved.class);
            exampleUserSolved.createCriteria().andEqualTo("username", username)
                    .andEqualTo("problemId", problemId);
            // 查询解答题目表中的记录
            UserProblemSolved userProblemSolved = userProblemSolvedMapper.selectOneByExample(exampleUserSolved);
            //如果用户第一次解决这题
            if (userProblemSolved.getSolvedCount() == 0) {
                currentProbInfo.setTotalAcUser(currentProbInfo.getTotalAcUser() + 1);
                userProblemSolved.setSolvedCount(1);
                userProblemSolved.setFirstSolvedTime(new Date());
                userBaseInfoMapper.updateACNumAddOne(username);
            }
            // 如果用户不是第一次解决了这题，前面已经解决过了
            else {
                userProblemSolved.setSolvedCount(userProblemSolved.getSolvedCount() + 1);
            }
            userProblemSolvedMapper.updateByPrimaryKeySelective(userProblemSolved);
            problemInfoMapper.updateByPrimaryKeySelective(currentProbInfo);

        }
    }

    /**
     * 处理本地评测机返回值的业务
     *
     * @param retJsonArr
     * @param judgeStatus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    protected String handleLocalJudgeReturns(JSONArray retJsonArr, JudgeStatus judgeStatus) {
        String ans;
        StringBuilder resultStr = new StringBuilder();
        String resStatus = "";
        int time = 0;
        int memory = 0;
        // 如果是计分类题，获得的得分
        int getScore = 0;
        // 是否是计分类题
        boolean isScore = false;
        for (int i = 0; i < retJsonArr.size(); i++) {
            resStatus = retJsonArr.getJSONArray(i).getString(1);
            resultStr.append("测试结果：【").append(resStatus).append("】 ");
            resultStr.append("测试文件：【").append(retJsonArr.getJSONArray(i).getString(0)).append("】 ");
            if ("SC".equals(resStatus)) {
                int score = retJsonArr.getJSONArray(i).getInteger(5);
                getScore += score;
                resultStr.append("得分：【").append(score).append("】 ");
                isScore = true;
                time += retJsonArr.getJSONArray(i).getInteger(2);
                resultStr.append("用时：【").append(retJsonArr.getJSONArray(i).getInteger(2)).append("MS】 ");
            } else if ("MLE".equals(resStatus) || "OLE".equals(resStatus)) {
                time += retJsonArr.getJSONArray(i).getInteger(4);
                resultStr.append("用时：【").append(retJsonArr.getJSONArray(i).getInteger(4)).append("MS】 ");
            } else {
                time += retJsonArr.getJSONArray(i).getInteger(2);
                resultStr.append("用时：【").append(retJsonArr.getJSONArray(i).getInteger(2)).append("MS】 ");
            }
            memory = Math.max(memory, retJsonArr.getJSONArray(i).getInteger(3));
            resultStr.append("内存：【").append(retJsonArr.getJSONArray(i).getInteger(3)).append("KB】\n");
        }
        JudgeResult judgeResult = new JudgeResult();
        judgeResult.setInfo(resultStr.toString());
        judgeResult.setJudgeId(judgeStatus.getId());
        judgeResult.setTime(new Date());
        judgeResultMapper.insertSelective(judgeResult);
        // 如果是得分题
        if (isScore) {
            // 设置得分
            int finalScore = getScore / retJsonArr.size();
            judgeStatus.setScore(finalScore);
            // 设置结果
            judgeStatus.setResult(SubmitResult.SC.getCode());
            ans = "SC " + finalScore;
        } else {
            judgeStatus.setResult(SubmitResult.valueOf(resStatus).getCode());
            ans = resStatus;
        }
        judgeStatus.setTimeUsed(time + "MS");
        judgeStatus.setMemoryUsed(memory + "KB");
        judgeStatusMapper.updateByPrimaryKeySelective(judgeStatus);
        return ans;
    }

    @Override
    public List<StatusCountVO> selectCountByDay(int days) {
        return judgeStatusMapper.selectCountByDay(days);
    }

    @Override
    public Integer selectCountByConditions(Integer contestId, String nick, Integer problemId, Integer result, Integer language) {
        return null;
    }

    @Override
    public JudgeStatusVO selectJudgeStatus(Integer id) {
        JudgeStatus judgeStatus = judgeStatusMapper.selectByPrimaryKey(id);
        JudgeStatusVO result = new JudgeStatusVO();
        result.setId(judgeStatus.getId());
        result.setCode(judgeStatus.getCode());
        result.setCodeLength(judgeStatus.getCodeLength());
        result.setLanguage(CodeLanguage.getNameByCode(judgeStatus.getLanguage()));
        result.setMemoryUsed(judgeStatus.getMemoryUsed());
        result.setTimeUsed(judgeStatus.getTimeUsed());
        Example example = new Example(UserCustomInfo.class);
        example.createCriteria().andEqualTo("username", judgeStatus.getUsername());
        String nickname = userCustomInfoMapper.selectOneByExample(example).getNickname();
        result.setNickname(nickname);
        result.setProblemId(judgeStatus.getProblemId());
        if (Objects.nonNull(judgeStatus.getScore())) {
            result.setResult(SubmitResult.getNameByCode(judgeStatus.getResult()) + " " + judgeStatus.getScore());
        } else {
            result.setResult(SubmitResult.getNameByCode(judgeStatus.getResult()));
        }
        result.setSubmitTime(judgeStatus.getSubmitTime());
        result.setUsername(judgeStatus.getUsername());
        return result;
    }

    @Override
    public Integer selectCountByUsername(String username) {
        return judgeStatusMapper.selectCountByUsername(username);
    }

    // add by zhongml [2020/4/27]
    @Override
    public JudgeStatus selectJudgeById(Integer id) {
        Example example = new Example(JudgeStatus.class);
        example.createCriteria().andEqualTo("id", id);
        return judgeStatusMapper.selectOneByExample(example);
    }

    @Override
    public List<LanguageUsedNumVO> countLanguageUsed() {
        List<LanguageUsedNumVO> languageUsedNumVOS = judgeStatusMapper.selectCountByLanguage();
        for (LanguageUsedNumVO vo : languageUsedNumVOS) {
            vo.setLanguageName(CodeLanguage.getNameByCode(vo.getLanguage()));
        }
        return languageUsedNumVOS;
    }

}
