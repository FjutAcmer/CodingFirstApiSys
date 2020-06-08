package team.fjut.cf.service.impl;

import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.SystemInfoMapper;
import team.fjut.cf.pojo.po.SystemInfo;
import team.fjut.cf.service.SystemInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author axiang [2019/10/30]
 */
@Service
public class SystemInfoServiceImpl implements SystemInfoService {
    @Resource
    SystemInfoMapper systemInfoMapper;


    @Override
    public int insert(SystemInfo systemInfo) {

        return systemInfoMapper.insertSelective(systemInfo);
    }

    @Override
    public Integer update(SystemInfo systemInfo) {

        return systemInfoMapper.updateByPrimaryKeySelective(systemInfo);
    }

    @Override
    public SystemInfo selectOne(String name) {
        Example example = new Example(SystemInfo.class);
        example.createCriteria().andEqualTo("name", name);
        return systemInfoMapper.selectOneByExample(example);
    }
}
