package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team.fjut.cf.mapper.UserProblemSolvedMapper;
import team.fjut.cf.mapper.ViewProblemInfoMapper;
import team.fjut.cf.pojo.enums.OjId;
import team.fjut.cf.pojo.enums.ProblemDifficultLevel;
import team.fjut.cf.pojo.po.UserProblemSolved;
import team.fjut.cf.pojo.po.ViewProblemInfo;
import team.fjut.cf.pojo.vo.ProblemListAdminVO;
import team.fjut.cf.pojo.vo.ProblemListVO;
import team.fjut.cf.service.ViewProblemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2020/3/5]
 */
@Service
public class ViewProblemInfoServiceImpl implements ViewProblemInfoService {
    @Resource
    ViewProblemInfoMapper viewProblemInfoMapper;

    @Resource
    UserProblemSolvedMapper userProblemSolvedMapper;

    @Override
    public List<ProblemListVO> pagesByConditions(int pageNum, int pageSize,
                                                 Integer problemId, String title, Integer tagId,
                                                 String username) {
        List<ProblemListVO> results = new ArrayList<>();
        // 下文要格式化用到的
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        boolean userIsLogin = false;
        HashMap<Integer, Integer> userSolveMap = new HashMap<>();
        // 如果是登录的，则需要补充解答信息
        if (!StringUtils.isEmpty(username)) {
            userIsLogin = true;
            Example example = new Example(UserProblemSolved.class);
            example.createCriteria().andEqualTo("username", username);
            // 首先拿到全部的用户解答信息
            List<UserProblemSolved> userProblemSolveds = userProblemSolvedMapper.selectByExample(example);
            for (UserProblemSolved item : userProblemSolveds) {
                userSolveMap.put(item.getProblemId(), item.getSolvedCount());
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(ViewProblemInfo.class);
        example.orderBy("problemId").asc();
        Example.Criteria criteria = example.createCriteria();
        // 非空则查询条件
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("problemId", problemId);
        }
        if (Objects.nonNull(title)) {
            criteria.andLike("title", title);
        }
        if (Objects.nonNull(tagId)) {
            //TODO:暂不选择TagId
        }
        List<ViewProblemInfo> viewProblemInfos = viewProblemInfoMapper.selectByExample(example);
        for (ViewProblemInfo item : viewProblemInfos) {
            ProblemListVO vo = new ProblemListVO();
            vo.setProblemId(item.getProblemId());
            vo.setBelongToOj(OjId.getNameByCode(item.getBelongOjId()));
            vo.setTitle(item.getTitle());
            vo.setDifficult(ProblemDifficultLevel.getNameByCode(item.getDifficultLevel()));
            if (userIsLogin) {
                Integer solvedCount = userSolveMap.get(item.getProblemId());
                if (Objects.nonNull(solvedCount)) {
                    vo.setIsSolved(solvedCount > 0 ? "yes" : "no");
                }
            }
            String ratio;
            if (item.getTotalSubmit() == 0) {
                ratio = "0.00%(0/0)";
            } else {
                ratio = decimalFormat.format(100.00 * item.getTotalAc() / item.getTotalSubmit()) + "%("
                        + item.getTotalAc() + "/" + item.getTotalSubmit() + ")";
            }
            vo.setRatio(ratio);
            results.add(vo);
        }
        return results;
    }

    @Override
    public int countByConditions(Integer problemId, String title, Integer tagId) {
        Example example = new Example(ViewProblemInfo.class);
        Example.Criteria criteria = example.createCriteria();
        // 非空则查询条件
        if (Objects.nonNull(problemId)) {
            criteria.andEqualTo("problemId", problemId);
        }
        if (Objects.nonNull(title)) {
            criteria.andLike("title", title);
        }
        if (Objects.nonNull(tagId)) {
            //TODO:暂不选择TagId
        }
        return viewProblemInfoMapper.selectCountByExample(example);
    }

    @Override
    public List<ProblemListAdminVO> selectByCondition(int pageNum, int pageSize, String sort, String title, Integer difficultLevel) {
        List<ProblemListAdminVO> results = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(ViewProblemInfo.class);

        if (sort != null && sort.equals("descending")) {
            example.orderBy("problemId").desc();
        } else {
            example.orderBy("problemId").asc();
        }
        Example.Criteria criteria = example.createCriteria();
        // 非空则查询条件
        if (Objects.nonNull(difficultLevel)) {
            criteria.andEqualTo("difficultLevel", difficultLevel);
        }
        if (Objects.nonNull(title)) {
            criteria.andLike("title", title);
        }
        List<ViewProblemInfo> viewProblemInfos = viewProblemInfoMapper.selectByExample(example);
        for (ViewProblemInfo item : viewProblemInfos) {
            ProblemListAdminVO vo = new ProblemListAdminVO();
            vo.setProblemId(item.getProblemId());
            vo.setBelongOj(OjId.getNameByCode(item.getBelongOjId()));
            vo.setTitle(item.getTitle());
            vo.setDifficulty(ProblemDifficultLevel.getNameByCode(item.getDifficultLevel()));
            vo.setVisible(item.getVisible());
            String ratio;
            if (item.getTotalSubmit() == 0) {
                ratio = "0.00%(0/0)";
            } else {
                ratio = decimalFormat.format(100.00 * item.getTotalAc() / item.getTotalSubmit()) + "%("
                        + item.getTotalAc() + "/" + item.getTotalSubmit() + ")";
            }
            vo.setRatio(ratio);
            results.add(vo);
        }
        return results;
    }

    @Override
    public int countByCondition(String title, Integer difficultLevel) {
        Example example = new Example(ViewProblemInfo.class);
        Example.Criteria criteria = example.createCriteria();
        // 非空则查询条件
        if (Objects.nonNull(difficultLevel)) {
            criteria.andEqualTo("difficultLevel", difficultLevel);
        }
        if (Objects.nonNull(title)) {
            criteria.andLike("title", title);
        }
        return viewProblemInfoMapper.selectCountByExample(example);
    }
}
