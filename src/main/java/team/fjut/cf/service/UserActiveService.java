package team.fjut.cf.service;

import java.util.List;

/**
 * @author zhongml [2020/05/13]
 */
public interface UserActiveService {
    /**
     * 统计用户活跃度与新用户
     *
     * @param pastDaysList
     * @return
     */
    Integer[] getUserActive(List<String> pastDaysList);


}
