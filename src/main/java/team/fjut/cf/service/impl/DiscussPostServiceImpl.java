package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.DiscussPostMapper;
import team.fjut.cf.mapper.DiscussReplyPostMapper;
import team.fjut.cf.pojo.po.DiscussPostPO;
import team.fjut.cf.pojo.po.DiscussReplyPostPO;
import team.fjut.cf.pojo.po.UserCustomInfo;
import team.fjut.cf.pojo.vo.DiscussPostVO;
import team.fjut.cf.pojo.vo.DiscussReplyPostVO;
import team.fjut.cf.pojo.vo.UserCustomInfoVO;
import team.fjut.cf.service.DiscussPostService;
import team.fjut.cf.service.UserCustomInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2020/2/21]
 */
@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Resource
    DiscussPostMapper discussPostMapper;

    @Resource
    DiscussReplyPostMapper discussReplyPostMapper;

    @Resource
    UserCustomInfoService userCustomInfoService;

    @Override
    public List<DiscussPostPO> pages(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<DiscussPostPO> discussPostPOS = discussPostMapper.all();
        return discussPostPOS;
    }

    @Override
    public Integer allCount() {
        return discussPostMapper.allCount();
    }

    // add by zhongml [2020/4/27]
    @Override
    public Integer countReplyById(Integer id) {
        Example example = new Example(DiscussReplyPostPO.class);
        example.createCriteria().andEqualTo("discussId", id);
        return discussReplyPostMapper.selectCountByExample(example);
    }

    // add by zhongml [2020/4/27]
    @Override
    public List<DiscussPostVO> selectByCondition(int pageNum, int pageSize, String sort, String title, String author) {
        List<DiscussPostVO> results = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(DiscussPostPO.class);
        if(sort != null && sort.equals("ascending")) {
            example.orderBy("time").asc();
        }
        else {
            example.orderBy("time").desc();
        }
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(title)) {
            criteria.andLike("title", title);
        }
        if (Objects.nonNull(author)) {
            criteria.andLike("author", author);
        }
        List<DiscussPostPO> discussPostPOS = discussPostMapper.selectByExample(example);
        for (DiscussPostPO item : discussPostPOS) {
            DiscussPostVO vo = new DiscussPostVO();
            vo.setId(item.getId());
            vo.setTitle(item.getTitle());
            vo.setAuthor(item.getAuthor());
            vo.setTime(item.getTime());
            vo.setPriority(item.getPriority());
            example = new Example(DiscussReplyPostPO.class);
            example.createCriteria().andEqualTo("discussId", item.getId());
            Integer replyNum = discussReplyPostMapper.selectCountByExample(example) - 1;
            vo.setReplyNum(replyNum);
            results.add(vo);
        }
        return results;
    }

    // add by zhongml [2020/4/27]
    @Override
    public Integer countByCondition(String title, String author) {
        Example example = new Example(DiscussPostPO.class);
        Example.Criteria criteria = example.createCriteria();
        if (Objects.nonNull(title)) {
            criteria.andLike("title", title);
        }
        if (Objects.nonNull(author)) {
            criteria.andLike("author", author);
        }
        return discussPostMapper.selectCountByExample(example);
    }

    // add by zhongml [2020/4/27]
    @Override
    public int deletePost(Integer id) {
        Example example = new Example(DiscussPostPO.class);
        example.createCriteria().andEqualTo("id", id);
        return discussPostMapper.deleteByExample(example);
    }

    @Override
    public int deleteReplyPost(Integer id) {
        Example example = new Example(DiscussReplyPostPO.class);
        example.createCriteria().andEqualTo("id", id);
        return discussReplyPostMapper.deleteByExample(example);
    }

    @Override
    public List<DiscussReplyPostVO> selectByPage(Integer pageNum, Integer pageSize, Integer id) {
        List<DiscussReplyPostVO> results = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(DiscussReplyPostPO.class);
        example.orderBy("replyOrder").asc();
        example.createCriteria().andEqualTo("discussId", id);
        List<DiscussReplyPostPO> discussReplyPostPOS = discussReplyPostMapper.selectByExample(example);
        for (DiscussReplyPostPO item : discussReplyPostPOS) {
            DiscussReplyPostVO vo = new DiscussReplyPostVO();
            vo.setId(item.getId());
            vo.setDiscussId(item.getDiscussId());
            vo.setAuthor(item.getAuthor());
            vo.setTime(item.getTime());
            vo.setReplyOrder(item.getReplyOrder());
            vo.setText(item.getText());
            UserCustomInfoVO userCustomInfo = userCustomInfoService.select(item.getAuthor());
            vo.setAvatarUrl(userCustomInfo.getAvatarUrl());
            results.add(vo);
        }
        return results;
    }
}
