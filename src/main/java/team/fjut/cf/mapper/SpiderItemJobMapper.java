package team.fjut.cf.mapper;

import org.apache.ibatis.annotations.Param;
import team.fjut.cf.pojo.po.SpiderItemJob;
import team.fjut.cf.pojo.vo.response.SpiderJobCountVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author axiang [2020/4/30]
 */
public interface SpiderItemJobMapper extends Mapper<SpiderItemJob> {
    List<SpiderJobCountVO> selectCountByDays(@Param("days") int days);
}
