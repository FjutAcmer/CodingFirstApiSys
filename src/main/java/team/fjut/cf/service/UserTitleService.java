package team.fjut.cf.service;

import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.vo.UserTitleInfoVO;

import java.util.List;

/**
 * @author zhongml [2020/4/28]
 */
public interface UserTitleService {

    /**
     * 查询全部称号
     *
     * @return
     * @author zhongml [2020/4/28]
     */
    List<UserTitle> selectAll();

    /**
     * 条件查询称号列表
     *
     * @param pageNum
     * @param sort
     * @param pageSize
     * @param name
     * @return
     * @author zhongml [2020/4/28]
     */
    List<UserTitle> pageByCondition(Integer pageNum, Integer pageSize, String sort, String name);

    /**
     * 条件查询称号数量
     *
     * @param name
     * @return
     * @author zhongml [2020/4/28]
     */
    int countByCondition(String name);

    /**
     * 查询用户称号记录
     *
     * @param username
     * @return
     * @author zhongml [2020/4/28]
     */
    List<UserTitleInfoVO> selectTitleByUsername(String username);

    /**
     * 添加用户称号记录
     *
     * @param userTitle
     * @return
     * @author zhongml [2020/4/28]
     */
    int createTitle(UserTitle userTitle);

    /**
     * 移除用户称号记录
     *
     * @param id
     * @return
     * @author zhongml [2020/4/28]
     */
    int deleteTitle(Integer id);
}
