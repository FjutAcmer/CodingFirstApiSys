package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import team.fjut.cf.mapper.BugReportedMapper;
import team.fjut.cf.pojo.enums.BugType;
import team.fjut.cf.pojo.po.BugReport;
import team.fjut.cf.pojo.vo.response.BugReportVO;
import team.fjut.cf.service.BugReportedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2019/11/15]
 */
@Service
public class BugReportedServiceImpl implements BugReportedService {
    @Resource
    BugReportedMapper bugReportedMapper;

    @Override
    public int insert(BugReport bugReport) {
        return bugReportedMapper.insert(bugReport);
    }

    @Override
    public List<BugReportVO> pageByCondition(Integer pageNum, Integer pageSize, String sort, Integer isFixed) {
        PageHelper.startPage(pageNum, pageSize);
        List<BugReportVO> results = new ArrayList<>();
        Example example = new Example(BugReport.class);
        if(sort != null && sort.equals("ascending")) {
            example.orderBy("reportTime").asc();
        }
        else {
            example.orderBy("reportTime").desc();
        }
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(isFixed)) {
            criteria.andEqualTo("isFixed", isFixed);
        }
        List<BugReport> bugReports = bugReportedMapper.selectByExample(example);
        for (BugReport item : bugReports) {
            BugReportVO vo = new BugReportVO();
            vo.setId(item.getId());
            vo.setUsername(item.getUsername());
            vo.setType(BugType.getNameByCode(item.getType()));
            vo.setCurrentPath(item.getCurrentPath());
            vo.setTitle(item.getTitle());
            vo.setText(item.getText());
            vo.setReportTime(item.getReportTime());
            vo.setIsFixed(item.getIsFixed());
            results.add(vo);
        }
        return results;
    }

    @Override
    public int countByCondition() {
        return bugReportedMapper.selectAll().size();
    }

    @Override
    public int setIdFixed(Integer id) {
        Example example = new Example(BugReport.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        BugReport bugReport = new BugReport();
        bugReport.setIsFixed(1);
        return bugReportedMapper.updateByExampleSelective(bugReport, example);
    }
}
