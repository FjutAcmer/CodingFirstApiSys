package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.JudgeStatusMapper;
import team.fjut.cf.mapper.ViewJudgeStatusMapper;
import team.fjut.cf.pojo.enums.CodeLanguage;
import team.fjut.cf.pojo.enums.SubmitResult;
import team.fjut.cf.pojo.po.JudgeStatus;
import team.fjut.cf.pojo.po.ViewJudgeStatus;
import team.fjut.cf.pojo.vo.StatusAdminVO;
import team.fjut.cf.pojo.vo.response.AcAndSubmitVO;
import team.fjut.cf.pojo.vo.response.StatusListVO;
import team.fjut.cf.service.ViewJudgeStatusService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2020/3/5]
 */
@Service
public class ViewJudgeStatusServiceImpl implements ViewJudgeStatusService {
    @Resource
    ViewJudgeStatusMapper viewJudgeStatusMapper;

    @Resource
    JudgeStatusMapper judgeStatusMapper;

    @Override
    public List<StatusListVO> pagesByConditions(Integer pageNum, Integer pageSize,
                                                Integer contestId,
                                                String nickname, Integer problemId, Integer result, Integer language) {
        List<StatusListVO> results = new ArrayList<>();
        Example example = new Example(ViewJudgeStatus.class);
        // 按id倒序
        example.orderBy("id").desc();
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(contestId)) {
            criteria.andEqualTo("contestId", contestId);
        }
        if (Objects.nonNull(nickname)) {
            criteria.andLike("nickname", nickname);
        }
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("problemId", problemId);
        }
        if (Objects.nonNull(result)) {
            criteria.andEqualTo("result", result);
        }
        if (Objects.nonNull(language)) {
            criteria.andEqualTo("language", language);
        }
        PageHelper.startPage(pageNum, pageSize, false);
        List<ViewJudgeStatus> viewJudgeStatuses = viewJudgeStatusMapper.selectByExample(example);
        for (ViewJudgeStatus item : viewJudgeStatuses) {
            StatusListVO temp = new StatusListVO();
            temp.setId(item.getId());
            temp.setNickname(item.getNickname());
            temp.setProblemId(item.getProblemId().toString());
            // 如果是Score的，带上分值
            if (item.getResult() == SubmitResult.SC.getCode()) {
                temp.setResult(SubmitResult.getNameByCode(item.getResult()) + " " + item.getScore());
            } else {
                temp.setResult(SubmitResult.getNameByCode(item.getResult()));
            }
            temp.setLanguage(CodeLanguage.getNameByCode(item.getLanguage()));
            temp.setTimeUsed(item.getTimeUsed());
            temp.setMemoryUsed(item.getMemoryUsed());
            temp.setCodeLength(item.getCodeLength());
            temp.setSubmitTime(item.getSubmitTime());
            results.add(temp);
        }
        return results;
    }

    @Override
    public int countByConditions(Integer contestId, String nickname, Integer problemId, Integer result, Integer language) {
        Example example = new Example(ViewJudgeStatus.class);
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(contestId)) {
            criteria.andEqualTo("contestId", contestId);
        }
        if (Objects.nonNull(nickname)) {
            criteria.andLike("nickname", nickname);
        }
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("problemId", problemId);
        }
        if (Objects.nonNull(result)) {
            criteria.andEqualTo("result", result);
        }
        if (Objects.nonNull(language)) {
            criteria.andEqualTo("language", language);
        }
        int i = viewJudgeStatusMapper.selectCountByExample(example);
        return i;
    }

    // add by zhongml [2020/4/27]
    @Override
    public List<StatusAdminVO> selectByPage(Integer pageNum, Integer pageSize, String sort, Integer contestId, String username, Integer problemId, Integer result, Integer language) {
        List<StatusAdminVO> results = new ArrayList<>();

        Example example = new Example(JudgeStatus.class);
        if (sort != null && sort.equals("ascending")) {
            example.orderBy("id").desc();
        } else {
            example.orderBy("id").desc();
        }
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(username)) {
            criteria.andLike("username", username);
        }
        if (Objects.nonNull(contestId)) {
            criteria.andEqualTo("contestId", contestId);
        }
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("problemId", problemId);
        }
        if (Objects.nonNull(result)) {
            criteria.andEqualTo("result", result);
        }
        if (Objects.nonNull(language)) {
            criteria.andEqualTo("language", language);
        }

        PageHelper.startPage(pageNum, pageSize, false);
        List<ViewJudgeStatus> viewJudgeStatuses = viewJudgeStatusMapper.selectByExample(example);
        for (ViewJudgeStatus item : viewJudgeStatuses) {
            StatusAdminVO vo = new StatusAdminVO();
            vo.setId(item.getId());
            vo.setUsername(item.getUsername());
            vo.setProblemId(item.getProblemId());
            vo.setContestId(item.getContestId());
            vo.setLanguage(CodeLanguage.getNameByCode(item.getLanguage()));
            vo.setSubmitTime(item.getSubmitTime());
            if (item.getResult() == SubmitResult.SC.getCode()) {
                vo.setResult(SubmitResult.getNameByCode(item.getResult()) + " " + item.getScore());
            } else {
                vo.setResult(SubmitResult.getNameByCode(item.getResult()));
            }
            vo.setMemoryUsed(item.getMemoryUsed());
            vo.setTimeUsed(item.getTimeUsed());
            vo.setCodeLength(item.getCodeLength());
            results.add(vo);
        }
        return results;
    }

    // add by zhongml [2020/4/27]
    @Override
    public int countByPage(Integer contestId, String username, Integer problemId, Integer result, Integer language) {
        Example example = new Example(JudgeStatus.class);
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(contestId)) {
            criteria.andEqualTo("contestId", contestId);
        }
        if (Objects.nonNull(username)) {
            criteria.andLike("username", username);
        }
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("problemId", problemId);
        }
        if (Objects.nonNull(result)) {
            criteria.andEqualTo("result", result);
        }
        if (Objects.nonNull(language)) {
            criteria.andEqualTo("language", language);
        }
        return judgeStatusMapper.selectCountByExample(example);
    }

    @Override
    public AcAndSubmitVO selectAcAndSubmit(List<String> pastDaysList) {
        AcAndSubmitVO acAndSubmitVO = new AcAndSubmitVO();
        Integer[] totalAc = new Integer[7];
        Integer[] totalSubmit = new Integer[7];
        Integer[] contestAc = new Integer[7];
        Integer[] contestSubmit = new Integer[7];
        for (int i = 0; i < 7; i ++) {
            totalAc[i] = judgeStatusMapper.countAcByDate(pastDaysList.get(i));
            totalSubmit[i] = judgeStatusMapper.countSubmitByDate(pastDaysList.get(i));
            contestAc[i] = judgeStatusMapper.countContestAcByDate(pastDaysList.get(i));
            contestSubmit[i] = judgeStatusMapper.countContestSubmitByDate(pastDaysList.get(i));
        }
        acAndSubmitVO.setTotalAc(totalAc);
        acAndSubmitVO.setTotalSubmit(totalSubmit);
        acAndSubmitVO.setContestAc(contestAc);
        acAndSubmitVO.setContestSubmit(contestSubmit);
        return acAndSubmitVO;
    }
}
