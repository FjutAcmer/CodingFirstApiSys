package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.vo.UserTitleInfoVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2019/11/12]
 */
public interface UserTitleMapper extends Mapper<UserTitle> {

    /**
     * 查询用户称号记录
     *
     * @param username
     * @return
     * @author zhongml [2020/4/28]
     */
    List<UserTitleInfoVO> selectByCondition(@Param("username") String username);

}
