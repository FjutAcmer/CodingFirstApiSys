package team.fjut.cf.service;

import team.fjut.cf.pojo.po.UserMessage;
import team.fjut.cf.pojo.vo.UserMessageListVO;

import java.util.List;

/**
 * @author axiang [2019/11/11]
 */
public interface UserMessageService {


    /**
     * 删除id为 messageId 的消息
     *
     * @param messageId
     * @return
     */
    int delete(int messageId);

    /**
     * 删除全部
     *
     * @param username
     * @return
     */
    int deleteAll(String username);

    /**
     * 设置消息状态
     *
     * @param messageId
     * @param status
     * @return
     */
    int setStatus(int messageId, int status);

    /**
     * 设置收件人为username的用户的全部消息状态
     *
     * @param username
     * @param status
     * @return
     */
    int setAllStatus(String username, int status);

    /**
     * 根据ID查询用户消息
     *
     * @param messageId
     * @return
     */
    UserMessage selectById(int messageId);

    /**
     * 根据条件分页查询用户消息记录
     *
     * @param pageNum
     * @param pageSize
     * @param toUsername
     * @param fromUsername
     * @param status
     * @param title
     * @return
     */
    List<UserMessageListVO> pagesByConditions(int pageNum, int pageSize,
                                              String toUsername,
                                              String fromUsername, Integer status, String title);


    /**
     * 根据条件分页查询用户消息记录数量
     *
     * @param toUsername
     * @param fromUsername
     * @param status
     * @param title
     * @return
     */
    int countByConditions(String toUsername,
                          String fromUsername, Integer status, String title);

    /**
     * 新增通知
     *
     * @param userMessage
     * @return
     */
    int insertMessage(UserMessage userMessage);

}
