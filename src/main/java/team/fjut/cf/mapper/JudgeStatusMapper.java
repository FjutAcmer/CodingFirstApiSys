package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.JudgeStatus;
import team.fjut.cf.pojo.vo.StatusCountVO;
import team.fjut.cf.pojo.vo.response.LanguageUsedNumVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/10/30]
 */
public interface JudgeStatusMapper extends Mapper<JudgeStatus> {
    /**
     * 查询最近 days 天的提交统计
     *
     * @param days
     * @return
     */
    List<StatusCountVO> selectCountByDay(@Param("days") int days);


    /**
     * 根据用户名查询评测记录条数
     *
     * @param username
     * @return
     */
    Integer selectCountByUsername(@Param("username") String username);

    /**
     * 查询某天用户总ac与提交量
     *
     * @param time
     * @return
     */
    Integer countAcByDate(@Param("time") String time);

    /**
     * 查询某天用户总ac与提交量
     *
     * @param time
     * @return
     */
    Integer countSubmitByDate(@Param("time") String time);

    /**
     * 查询某天比赛的ac与提交量
     *
     * @param time
     * @return
     */
    Integer countContestAcByDate(@Param("time") String time);

    /**
     * 查询某天比赛的ac与提交量
     *
     * @param time
     * @return
     */
    Integer countContestSubmitByDate(@Param("time") String time);

    /**
     * 查询语言使用次数
     *
     * @return
     */
    List<LanguageUsedNumVO> selectCountByLanguage();
}
