package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import team.fjut.cf.mapper.BugReportedMapper;
import team.fjut.cf.pojo.po.BugReport;
import team.fjut.cf.service.BugReportedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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
    public List<BugReport> pageByCondition(Integer pageNum, Integer pageSize, String sort) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(BugReport.class);
        if(sort != null && sort.equals("ascending")) {
            example.orderBy("reportTime").asc();
        }
        else {
            example.orderBy("reportTime").desc();
        }
        return bugReportedMapper.selectByExample(example);
    }

    @Override
    public int countByCondition() {
        return bugReportedMapper.selectAll().size();
    }
}
