package team.fjut.cf.service;

import team.fjut.cf.pojo.po.JudgeStatus;
import team.fjut.cf.pojo.vo.JudgeStatusVO;
import team.fjut.cf.pojo.vo.StatusCountVO;
import team.fjut.cf.pojo.vo.response.LanguageUsedNumVO;

import java.util.List;

/**
 * @author axiang [2019/10/30]
 */
public interface JudgeStatusService {
    /**
     * 插入一条评测记录
     *
     * @param judgeStatus
     * @return
     */
    Integer insert(JudgeStatus judgeStatus);

    /**
     * 如果提交代码成功更新数据库内容
     * 更新题目表
     * 更新用户解答记录表
     *
     * @param judgeStatus
     * @return
     */
    Boolean ifSubmitSuccess(JudgeStatus judgeStatus);

    /**
     * 如果评测机出现异常
     *
     * @param judgeStatus
     * @return
     */
    void ifLocalJudgeError(JudgeStatus judgeStatus);

    /**
     * 如果提交异常
     *
     * @param judgeStatus
     * @return
     */
    void ifSubmitError(JudgeStatus judgeStatus);

    /**
     * 查询本地评测机中的结果，异步做法
     *
     * @param judgeStatus
     * @throws Exception
     */
    void queryResultFromLocalJudge(JudgeStatus judgeStatus) throws Exception;

    /**
     * 查询最近 days 天的提交统计
     *
     * @param days
     * @return
     */
    List<StatusCountVO> selectCountByDay(int days);


    /**
     * 查询评测列表视图的大小
     *
     * @param contestId
     * @param nick
     * @param problemId
     * @param result
     * @param language
     * @return
     */
    Integer selectCountByConditions(Integer contestId, String nick, Integer problemId, Integer result, Integer language);

    /**
     * 根据评测ID查询评测列表视图
     *
     * @param id
     * @return
     */
    JudgeStatusVO selectJudgeStatus(Integer id);

    /**
     * 根据用户名查询评测记录条数
     *
     * @param username
     * @return
     */
    Integer selectCountByUsername(String username);

    /**
     * @param id
     * @return
     * @author zhongml [2020/4/27]
     * 根据ID查询评测信息
     */
    JudgeStatus selectJudgeById(Integer id);

    /**
     * @return
     * @author zhongml [2020/5/13]
     * 查询各语言使用次数
     */
    List<LanguageUsedNumVO> countLanguageUsed();

}
