package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.ContestProblemMapper;
import team.fjut.cf.mapper.ContestRegisterMapper;
import team.fjut.cf.pojo.enums.ContestKind;
import team.fjut.cf.pojo.enums.ContestReviewStatus;
import team.fjut.cf.pojo.po.ContestProblemPO;
import team.fjut.cf.pojo.po.ContestRegisterUserPO;
import team.fjut.cf.pojo.vo.ContestRegisterUserVO;
import team.fjut.cf.service.ContestInfoService;
import team.fjut.cf.service.ContestProblemService;
import team.fjut.cf.service.ContestRegisterService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhongml [2020/4/23]
 */
@Service
public class ContestRegisterServiceImpl implements ContestRegisterService {
    @Resource
    ContestRegisterMapper contestRegisterMapper;

    @Resource
    ContestInfoService contestInfoService;

    @Override
    public List<ContestRegisterUserVO> pagesByConditions(Integer pageNum, Integer pageSize, String sort, Integer contestKind, Integer reviewStatus, String username, Integer contestId) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(ContestRegisterUserPO.class);
        List<ContestRegisterUserVO> result = new ArrayList<>();
        if(sort != null && sort.equals("descending")) {
            example.orderBy("contestId").desc();
        }
        else {
            example.orderBy("contestId").asc();
        }
        Example.Criteria criteria = example.createCriteria();

        if (Objects.nonNull(contestKind)) {
            criteria.andEqualTo("contestKind", contestKind);
        }
        if (Objects.nonNull(contestKind)) {
            criteria.andEqualTo("contestId", contestId);
        }
        if (Objects.nonNull(reviewStatus)) {
            criteria.andEqualTo("reviewStatus", reviewStatus);
        }
        if (Objects.nonNull(username)) {
            criteria.andLike("username", username);
        }

        List<ContestRegisterUserPO> contestRegisterUserPOS = contestRegisterMapper.selectByExample(example);
        for (ContestRegisterUserPO item : contestRegisterUserPOS) {
            ContestRegisterUserVO contestRegisterUser = new ContestRegisterUserVO();
            contestRegisterUser.setId(item.getId());
            contestRegisterUser.setContestId(item.getContestId());
            String title = contestInfoService.selectByContestId(item.getContestId()).getTitle();
            Integer kind = contestInfoService.selectByContestId(item.getContestId()).getContestKind();
            contestRegisterUser.setTitle(title);
            contestRegisterUser.setContestKind(ContestKind.getNameByCode(kind));
            contestRegisterUser.setUsername(item.getUsername());
            contestRegisterUser.setRegisterTime(item.getRegisterTime());
            contestRegisterUser.setReviewTime(item.getReviewTime());
            contestRegisterUser.setReviewInfo(item.getReviewInfo());
            contestRegisterUser.setReviewStatus(ContestReviewStatus.getNameById(item.getReviewStatus()));
            result.add(contestRegisterUser);
        }
        return result;
    }

    @Override
    public Integer selectCountByConditions(Integer contestKind, Integer reviewStatus, String username) {
        Example example = new Example(ContestRegisterUserPO.class);
        Example.Criteria criteria = example.createCriteria();

        if (Objects.nonNull(contestKind)) {
            criteria.andEqualTo("contestKind", contestKind);
        }
        if (Objects.nonNull(reviewStatus)) {
            criteria.andEqualTo("reviewStatus", reviewStatus);
        }
        if (Objects.nonNull(username)) {
            criteria.andLike("username", username);
        }
        return contestRegisterMapper.selectCountByExample(example);
    }

    @Override
    public Integer updateReviewStatus(ContestRegisterUserPO contestRegisterUserPO) {
        Example example = new Example(ContestRegisterUserPO.class);
        example.createCriteria().andEqualTo("id", contestRegisterUserPO.getId());
        return contestRegisterMapper.updateByExampleSelective(contestRegisterUserPO, example);
    }
}
