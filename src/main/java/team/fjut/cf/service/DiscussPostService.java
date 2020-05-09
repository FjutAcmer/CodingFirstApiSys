package team.fjut.cf.service;

import team.fjut.cf.pojo.po.DiscussPostPO;
import team.fjut.cf.pojo.vo.DiscussPostVO;
import team.fjut.cf.pojo.vo.DiscussReplyPostVO;

import java.util.List;

/**
 * @author axiang
 */
public interface DiscussPostService {
    /**
     * 分页查找聊天帖子
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<DiscussPostPO> pages(int pageNum, int pageSize);

    /**
     * 查询全部聊天帖子数量
     *
     * @return
     */
    Integer allCount();

    /**
     * @author zhongml [2020/4/27]
     * 查询全部聊天回复数量
     *
     * @return
     */
    Integer countReplyById(Integer id);

    /**
     * @author zhongml [2020/4/27]
     * 条件查询聊天帖子
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<DiscussPostVO> selectByCondition(int pageNum, int pageSize, String sort, String title, String author);

    /**
     * @author zhongml [2020/4/27]
     * 条件查询聊天数量
     *
     * @return
     */
    Integer countByCondition(String title, String author);

    /**
     * @author zhongml [2020/4/27]
     * 根据ID删除聊天
     *
     * @param id
     * @return
     */
    int deletePost(Integer id);

    /**
     * @author zhongml [2020/4/27]
     * 根据ID删除回复
     *
     * @param id
     * @return
     */
    int deleteReplyPost(Integer id);

    /**
     * @author zhongml [2020/4/27]
     * 根据ID获取所有回复
     *
     * @param pageNum
     * @param pageSize
     * @param id
     * @return
     */
    List<DiscussReplyPostVO> selectByPage(Integer pageNum, Integer pageSize, Integer id);
}
