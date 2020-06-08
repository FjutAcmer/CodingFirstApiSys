package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.component.judge.vjudge.pojo.enums.LanguageType;
import team.fjut.cf.component.judge.vjudge.pojo.enums.StatusType;
import team.fjut.cf.mapper.UserCustomInfoMapper;
import team.fjut.cf.mapper.ViewVjJudgeResultMapper;
import team.fjut.cf.pojo.po.ViewVjJudgeResult;
import team.fjut.cf.pojo.po.VjJudgeResult;
import team.fjut.cf.pojo.vo.VjJudgeResultVO;
import team.fjut.cf.service.ViewVjJudgeResultService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2020/3/16]
 */
@Service
public class ViewVjJudgeResultServiceImpl implements ViewVjJudgeResultService {

    @Resource
    ViewVjJudgeResultMapper viewVjJudgeResultMapper;

    @Resource
    UserCustomInfoMapper userCustomInfoMapper;


    @Override
    public List<VjJudgeResultVO> pagesByConditions(int pageNum, int pageSize,
                                                   String nickname,
                                                   String problemId,
                                                   Integer status,
                                                   String language) {
        List<VjJudgeResultVO> results = new ArrayList<>();
        Example example = new Example(ViewVjJudgeResult.class);
        example.orderBy("id").desc();
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(nickname)) {
            criteria.andEqualTo("nickname", nickname);
        }
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("probNum", problemId);
        }
        if (Objects.nonNull(status)) {
            if (status.equals(StatusType.AC.getKey())) {
                criteria.andEqualTo("statusCanonical", "AC");
            } else if (status.equals(StatusType.PE.getKey())) {
                criteria.andEqualTo("statusCanonical", "PE");
            } else if (status.equals(StatusType.WA.getKey())) {
                criteria.andEqualTo("statusCanonical", "WA");
            } else if (status.equals(StatusType.TLE.getKey())) {
                criteria.andEqualTo("statusCanonical", "TLE");
            } else if (status.equals(StatusType.MLE.getKey())) {
                criteria.andEqualTo("statusCanonical", "MLE");
            } else if (status.equals(StatusType.OLE.getKey())) {
                criteria.andEqualTo("statusCanonical", "MLE");
            } else if (status.equals(StatusType.RE.getKey())) {
                criteria.andEqualTo("statusCanonical", "RE");
            } else if (status.equals(StatusType.CE.getKey())) {
                criteria.andEqualTo("statusCanonical", "CE");
            } else if (status.equals(StatusType.UE.getKey())) {
                criteria.andEqualTo("statusCanonical", "FAILED_OTHER");
            } else if (status.equals(StatusType.SE.getKey())) {
                criteria.andEqualTo("statusCanonical", "SUBMIT_FAILED_TEMP");
            } else if (status.equals(StatusType.Q_AND_G.getKey())) {
                criteria.andEqualTo("statusCanonical", "QUEUEING")
                        .orEqualTo("statusCanonical", "JUDGING");
            }
        }
        if (Objects.nonNull(language)) {
            if (LanguageType.OTHER.getKey().equals(language)) {
                criteria.andNotEqualTo("languageCanonical", LanguageType.C.getKey())
                        .andNotEqualTo("languageCanonical", LanguageType.CPP.getKey())
                        .andNotEqualTo("languageCanonical", LanguageType.CSHARP.getKey())
                        .andNotEqualTo("languageCanonical", LanguageType.JAVA.getKey())
                        .andNotEqualTo("languageCanonical", LanguageType.PASCAL.getKey())
                        .andNotEqualTo("languageCanonical", LanguageType.PYTHON.getKey())
                        .andNotEqualTo("languageCanonical", LanguageType.RUBY.getKey());
            } else {
                criteria.andEqualTo("languageCanonical", language);
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<ViewVjJudgeResult> viewVjJudgeResults = viewVjJudgeResultMapper.selectByExample(example);
        for (ViewVjJudgeResult item : viewVjJudgeResults) {
            VjJudgeResultVO temp = new VjJudgeResultVO();
            temp.setId(item.getId());
            temp.setRunId(item.getRunId());
            temp.setUsername(item.getUsername());
            temp.setNickname(item.getNickname());
            temp.setSubmitTime(item.getSubmitTime());
            temp.setRuntime(item.getRuntime());
            temp.setStatus(item.getStatus());
            temp.setProb(item.getProbNum());
            temp.setOj(item.getOj());
            temp.setMemory(item.getMemory());
            temp.setLength(item.getLength());
            temp.setLanguage(item.getLanguage());
            results.add(temp);
        }
        return results;

    }


    @Override
    public int countByConditions(String nickname, String problemId, Integer status, String language) {
        Example example = new Example(VjJudgeResult.class);
        return viewVjJudgeResultMapper.selectCountByExample(example);
    }
}
