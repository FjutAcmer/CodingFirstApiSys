package team.fjut.cf.service;

import team.fjut.cf.pojo.po.BugReport;

import java.util.List;

/**
 * @author axiang [2019/11/15]
 */
public interface BugReportedService {
    /**
     * 插入bug反馈记录
     *
     * @param bugReport
     * @return
     */
    int insert(BugReport bugReport);

    /**
     * 查看bug反馈记录
     * @author zhongml [2020/5/12]
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @return
     */
    List<BugReport> pageByCondition(Integer pageNum, Integer pageSize, String sort);

    /**
     * 查看bug反馈记录
     * @author zhongml [2020/5/12]
     *
     * @return
     */
    int countByCondition();
}
