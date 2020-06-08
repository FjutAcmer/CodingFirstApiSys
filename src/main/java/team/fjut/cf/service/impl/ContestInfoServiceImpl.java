package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.ContestInfoMapper;
import team.fjut.cf.pojo.enums.ContestKind;
import team.fjut.cf.pojo.enums.ContestPermission;
import team.fjut.cf.pojo.po.ContestInfoPO;
import team.fjut.cf.pojo.vo.ContestInfoVO;
import team.fjut.cf.pojo.vo.ContestListVO;
import team.fjut.cf.pojo.vo.response.ContestTypeVO;
import team.fjut.cf.service.ContestInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author axiang [2019/11/18]
 */
@Service
public class ContestInfoServiceImpl implements ContestInfoService {
    @Resource
    ContestInfoMapper contestInfoMapper;

    @Override
    public List<ContestListVO> pagesByConditions(Integer kind,
                                                 String searchTitle,
                                                 Integer searchPermission,
                                                 Integer searchStatus,
                                                 Integer pageNum,
                                                 Integer pageSize) {
        List<ContestListVO> result = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        List<ContestInfoPO> contestInfos = contestInfoMapper.selectByConditions(kind, searchTitle, searchPermission, searchStatus);
        for (ContestInfoPO contestInfo : contestInfos) {
            ContestListVO contestList = new ContestListVO();
            contestList.setId(contestInfo.getContestId());
            contestList.setTitle(contestInfo.getTitle());
            contestList.setKind(ContestKind.getNameByCode(contestInfo.getContestKind()));
            contestList.setBeginTime(contestInfo.getBeginTime());
            contestList.setEndTime(contestInfo.getEndTime());
            contestList.setCreateUser(contestInfo.getCreateUser());
            contestList.setPermission(ContestPermission.getNameByCode(contestInfo.getPermissionType()));
            Date currentTime = new Date();
            if (contestInfo.getEndTime().compareTo(currentTime) < 0) {
                contestList.setStatus("已结束");
            } else if (contestInfo.getBeginTime().compareTo(currentTime) >= 0) {
                contestList.setStatus("未开始");
            } else {
                contestList.setStatus("正在进行");
            }
            result.add(contestList);
        }
        return result;
    }

    @Override
    public Integer selectCountByConditions(Integer kind,
                                           String searchTitle,
                                           Integer searchPermission,
                                           Integer searchStatus) {
        return contestInfoMapper.selectCountByConditions(kind, searchTitle, searchPermission, searchStatus);
    }

    @Override
    public ContestInfoPO selectByContestId(Integer contestId) {
        return contestInfoMapper.selectByContestId(contestId);
    }

    @Override
    public List<ContestInfoPO> selectAll() {
        Example example = new Example(ContestInfoPO.class);
        example.orderBy("contestId").desc();
        return  contestInfoMapper.selectByExample(example);
    }


    @Override
    public Integer createContest(ContestInfoVO newContestVO) {
        ContestInfoPO contestInfoPO = new ContestInfoPO();
        contestInfoPO.setTitle(newContestVO.getTitle());
        contestInfoPO.setContestId(newContestVO.getId());
        contestInfoPO.setContestKind(newContestVO.getContestKind());
        contestInfoPO.setBeginTime(newContestVO.getBeginTime());
        contestInfoPO.setEndTime(newContestVO.getEndTime());
        contestInfoPO.setRegisterBeginTime(newContestVO.getRegisterBeginTime());
        contestInfoPO.setRegisterEndTime(newContestVO.getRegisterEndTime());
        contestInfoPO.setPermissionType(newContestVO.getPermissionType());
        contestInfoPO.setDescription(newContestVO.getDescription());
        contestInfoPO.setPassword(newContestVO.getPassword());
        contestInfoPO.setComputerRating(newContestVO.getComputerRating());
        contestInfoPO.setRankType(newContestVO.getRankType());
        contestInfoPO.setProblemPutTag(newContestVO.getProblemPutTag());
        contestInfoPO.setStatusReadOut(newContestVO.getStatusReadOut());
        contestInfoPO.setShowRegisterList(newContestVO.getShowRegisterList());
        contestInfoPO.setShowBorderList(newContestVO.getShowBorderList());
        contestInfoPO.setShowOtherStatus(newContestVO.getShowOtherStatus());
        contestInfoPO.setCreateUser(newContestVO.getCreateUser());
        return contestInfoMapper.insertSelective(contestInfoPO);
    }

    @Override
    public Integer updateContest(ContestInfoVO newContestVO) {
        ContestInfoPO contestInfoPO = new ContestInfoPO();
        contestInfoPO.setTitle(newContestVO.getTitle());
        contestInfoPO.setContestKind(newContestVO.getContestKind());
        contestInfoPO.setBeginTime(newContestVO.getBeginTime());
        contestInfoPO.setEndTime(newContestVO.getEndTime());
        contestInfoPO.setRegisterBeginTime(newContestVO.getRegisterBeginTime());
        contestInfoPO.setRegisterEndTime(newContestVO.getRegisterEndTime());
        contestInfoPO.setPermissionType(newContestVO.getPermissionType());
        contestInfoPO.setDescription(newContestVO.getDescription());
        contestInfoPO.setPassword(newContestVO.getPassword());
        contestInfoPO.setComputerRating(newContestVO.getComputerRating());
        contestInfoPO.setRankType(newContestVO.getRankType());
        contestInfoPO.setProblemPutTag(newContestVO.getProblemPutTag());
        contestInfoPO.setStatusReadOut(newContestVO.getStatusReadOut());
        contestInfoPO.setShowRegisterList(newContestVO.getShowRegisterList());
        contestInfoPO.setShowBorderList(newContestVO.getShowBorderList());
        contestInfoPO.setShowOtherStatus(newContestVO.getShowOtherStatus());
        Example example = new Example(ContestInfoPO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("contestId", newContestVO.getId());
        return contestInfoMapper.updateByExampleSelective(contestInfoPO, example);
    }

    @Override
    public Integer countContestInProgress() {
        return contestInfoMapper.selectCountInProgress();
    }

    @Override
    public List<ContestTypeVO> getContestTypeCount() {
        List<ContestTypeVO> contestTypeVOS = contestInfoMapper.selectContestTypeCount();
        for (ContestTypeVO vo : contestTypeVOS) {
            vo.setContestTypeName(ContestKind.getNameByCode(vo.getContestType()));
        }
        return contestTypeVOS;
    }


}
