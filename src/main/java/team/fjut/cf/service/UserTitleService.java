package team.fjut.cf.service;

import team.fjut.cf.mapper.UserTitleMapper;
import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.po.UserTitleRecordPO;
import team.fjut.cf.pojo.vo.UserTitleInfoVO;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zhongml [2020/4/28]
 */
public interface UserTitleService {

    /**
     * @author zhongml [2020/4/28]
     * 查询全部称号
     *
     * @return
     */
    List<UserTitle> selectAll();

    /**
     * @author zhongml [2020/4/28]
     * 条件查询称号列表
     *
     * @param pageNum
     * @param sort 
     * @param pageSize
     * @param name
     * @return
     */
    List<UserTitle> pageByCondition(Integer pageNum, Integer pageSize, String sort, String name);

    /**
     * @author zhongml [2020/4/28]
     * 条件查询称号数量
     *
     * @param name
     * @return
     */
    int countByCondition(String name);

    /**
     * @author zhongml [2020/4/28]
     * 查询用户称号记录
     *
     * @param username
     * @return
     */
    List<UserTitleInfoVO> selectTitleByUsername(String username);

    /**
     * @author zhongml [2020/4/28]
     * 添加用户称号记录
     *
     * @param userTitle
     * @return
     */
    int createTitle(UserTitle userTitle);

    /**
     * @author zhongml [2020/4/28]
     * 移除用户称号记录
     *
     * @param id
     * @return
     */
    int deleteTitle(Integer id);
}
