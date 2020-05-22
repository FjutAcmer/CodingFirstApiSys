package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.BorderHonorRankMapper;
import team.fjut.cf.pojo.enums.AwardLevel;
import team.fjut.cf.pojo.enums.ContestLevel;
import team.fjut.cf.pojo.po.BorderHonorRankPO;
import team.fjut.cf.pojo.vo.response.BorderHonorRankVO;
import team.fjut.cf.service.BorderHonorRankService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2019/11/11]
 */
@Service
public class BorderHonorRankServiceImpl implements BorderHonorRankService {
    @Resource
    BorderHonorRankMapper borderHonorRankMapper;

    @Override
    public List<BorderHonorRankVO> pages(Integer pageNum, Integer pageSize, String sort, String realName, Integer awardLevel, Integer contestLevel) {
        List<BorderHonorRankVO> results = new ArrayList<>();
        /*
         *  mod by zhongml （修改为条件查询，根据条件返回升降序数据，默认降序）
         */
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(BorderHonorRankPO.class);
        if(sort != null && sort.equals("ascending")) {
            example.orderBy("rewardDate").asc();
        }
        else {
            example.orderBy("rewardDate").desc();
        }
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(realName)) {
            criteria.andLike("realNameOne", realName);
            criteria.andLike("realNameTwo", realName);
            criteria.andLike("realNameThree", realName);
        }
        if (Objects.nonNull(awardLevel)) {
            criteria.andEqualTo("awardLevel", awardLevel);
        }
        if (Objects.nonNull(contestLevel)) {
            criteria.andEqualTo("contestLevel", contestLevel);
        }
        List<BorderHonorRankPO> borderHonorRankPOS = borderHonorRankMapper.selectByExample(example);
        for (BorderHonorRankPO item : borderHonorRankPOS) {
            BorderHonorRankVO borderHonorRankVO = new BorderHonorRankVO();
            borderHonorRankVO.setContestLevel(ContestLevel.getNameByCode(item.getContestLevel()));
            borderHonorRankVO.setAwardLevel(AwardLevel.getNameByCode(item.getAwardLevel()));
            borderHonorRankVO.setDescription(item.getDescription());
            borderHonorRankVO.setId(item.getId());
            borderHonorRankVO.setRealNameOne(item.getRealNameOne());
            borderHonorRankVO.setRealNameTwo(item.getRealNameTwo());
            borderHonorRankVO.setRealNameThree(item.getRealNameThree());
            borderHonorRankVO.setUsernameOne(item.getUsernameOne());
            borderHonorRankVO.setUsernameTwo(item.getUsernameTwo());
            borderHonorRankVO.setUsernameThree(item.getUsernameThree());
            borderHonorRankVO.setRewardDate(item.getRewardDate());
            borderHonorRankVO.setRegisterTime(item.getRegisterTime());
            results.add(borderHonorRankVO);
        }
        return results;
    }

    // add by zhongml [2020/4/26]
    @Override
    public Integer countByCondition(String realName, Integer awardLevel, Integer contestLevel) {
        Example example = new Example(BorderHonorRankPO.class);
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(realName)) {
            criteria.andLike("realNameOne", realName);
            criteria.andLike("realNameTwo", realName);
            criteria.andLike("realNameThree", realName);
        }
        if (Objects.nonNull(awardLevel)) {
            criteria.andEqualTo("awardLevel", awardLevel);
        }
        if (Objects.nonNull(contestLevel)) {
            criteria.andEqualTo("contestLevel", contestLevel);
        }
        return borderHonorRankMapper.selectCountByExample(example);
    }

    @Override
    public int updateHonor(BorderHonorRankVO vo) {
        BorderHonorRankPO borderHonorRankPO = new BorderHonorRankPO();
        borderHonorRankPO.setId(vo.getId());
        borderHonorRankPO.setAwardLevel(AwardLevel.getCodeByName(vo.getAwardLevel()));
        borderHonorRankPO.setContestLevel(ContestLevel.getCodeByName(vo.getContestLevel()));
        borderHonorRankPO.setRealNameOne(vo.getRealNameOne());
        borderHonorRankPO.setRealNameTwo(vo.getRealNameTwo());
        borderHonorRankPO.setRealNameThree(vo.getRealNameThree());
        borderHonorRankPO.setUsernameOne(vo.getUsernameOne());
        borderHonorRankPO.setUsernameTwo(vo.getUsernameTwo());
        borderHonorRankPO.setUsernameThree(vo.getUsernameThree());
        borderHonorRankPO.setDescription(vo.getDescription());
        borderHonorRankPO.setRegisterTime(vo.getRegisterTime());
        borderHonorRankPO.setRewardDate(vo.getRewardDate());

        Example example = new Example(BorderHonorRankPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", vo.getId());
        return borderHonorRankMapper.updateByExampleSelective(borderHonorRankPO, example);
    }

    @Override
    public int insertHonor(BorderHonorRankVO vo) {
        BorderHonorRankPO borderHonorRankPO = new BorderHonorRankPO();
        borderHonorRankPO.setAwardLevel(AwardLevel.getCodeByName(vo.getAwardLevel()));
        borderHonorRankPO.setContestLevel(ContestLevel.getCodeByName(vo.getContestLevel()));
        borderHonorRankPO.setRealNameOne(vo.getRealNameOne());
        borderHonorRankPO.setRealNameTwo(vo.getRealNameTwo());
        borderHonorRankPO.setRealNameThree(vo.getRealNameThree());
        borderHonorRankPO.setUsernameOne(vo.getUsernameOne());
        borderHonorRankPO.setUsernameTwo(vo.getUsernameTwo());
        borderHonorRankPO.setUsernameThree(vo.getUsernameThree());
        borderHonorRankPO.setDescription(vo.getDescription());
        borderHonorRankPO.setRegisterTime(vo.getRegisterTime());
        borderHonorRankPO.setRewardDate(vo.getRewardDate());

        return borderHonorRankMapper.insertSelective(borderHonorRankPO);
    }

    @Override
    public BorderHonorRankVO selectById(Integer id) {
        Example example = new Example(BorderHonorRankPO.class);
        example.createCriteria().andEqualTo("id", id);
        BorderHonorRankPO po = borderHonorRankMapper.selectOneByExample(example);
        BorderHonorRankVO borderHonorRankVO = new BorderHonorRankVO();
        borderHonorRankVO.setContestLevel(ContestLevel.getNameByCode(po.getContestLevel()));
        borderHonorRankVO.setAwardLevel(AwardLevel.getNameByCode(po.getAwardLevel()));
        borderHonorRankVO.setDescription(po.getDescription());
        borderHonorRankVO.setId(po.getId());
        borderHonorRankVO.setRealNameOne(po.getRealNameOne());
        borderHonorRankVO.setRealNameTwo(po.getRealNameTwo());
        borderHonorRankVO.setRealNameThree(po.getRealNameThree());
        borderHonorRankVO.setUsernameOne(po.getUsernameOne());
        borderHonorRankVO.setUsernameTwo(po.getUsernameTwo());
        borderHonorRankVO.setUsernameThree(po.getUsernameThree());
        borderHonorRankVO.setRewardDate(po.getRewardDate());
        borderHonorRankVO.setRegisterTime(po.getRegisterTime());
        return borderHonorRankVO;
    }

    @Override
    public int deleteHonor(Integer id) {
        Example example = new Example(BorderHonorRankPO.class);
        example.createCriteria().andEqualTo("id", id);
        return borderHonorRankMapper.deleteByExample(example);
    }

    @Override
    public Integer selectAllCount() {
        return borderHonorRankMapper.allCount();
    }

    @Override
    public List<String> selectByUsername(String username) {
        List<String> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<BorderHonorRankPO> borderHonorRankPOS = borderHonorRankMapper.selectByUsername(username);
        for (BorderHonorRankPO honorRankPO : borderHonorRankPOS) {
            String str = "";
            str = format.format(honorRankPO.getRewardDate()) + ": 参加 " +
                    ContestLevel.getNameByCode(honorRankPO.getContestLevel())
                    + " 获得 " + AwardLevel.getNameByCode(honorRankPO.getAwardLevel())
                    + " " + honorRankPO.getDescription();
            result.add(str);
        }
        return result;
    }
}
