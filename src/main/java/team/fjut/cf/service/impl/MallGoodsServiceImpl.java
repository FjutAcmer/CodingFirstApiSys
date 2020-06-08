package team.fjut.cf.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.MallGoodsMapper;
import team.fjut.cf.pojo.po.MallGoods;
import team.fjut.cf.service.MallGoodsService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author axiang [2019/11/12]
 */
@Service
public class MallGoodsServiceImpl implements MallGoodsService {
    @Resource
    MallGoodsMapper mallGoodsMapper;

    @Override
    public List<MallGoods> pages(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<MallGoods> mallGoods = mallGoodsMapper.all();
        return mallGoods;
    }

    @Override
    public Integer selectAllCount() {
        return mallGoodsMapper.allCount();
    }

    @Override
    public MallGoods selectByGoodsId(Integer id) {
        return mallGoodsMapper.selectByGoodsId(id);
    }


    @Override
    public List<MallGoods> selectByCondition(Integer pageNum, Integer pageSize, String sort, Integer id, String name) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(MallGoods.class);

        if (sort != null && sort.equals("descending")) {
            example.orderBy("id").desc();
        } else {
            example.orderBy("id").asc();
        }
        Example.Criteria criteria = example.createCriteria();

        if (Objects.nonNull(id)) {
            criteria.andEqualTo("id", id);
        }
        if (Objects.nonNull(name)) {
            criteria.andLike("name", name);
        }

        List<MallGoods> result = mallGoodsMapper.selectByExample(example);
        return result;
    }

    @Override
    public int countByCondition(Integer id, String name) {
        Example example = new Example(MallGoods.class);
        Example.Criteria criteria = example.createCriteria();
        // 非空则查询条件
        if (Objects.nonNull(id)) {
            criteria.andEqualTo("id", id);
        }
        if (Objects.nonNull(name)) {
            criteria.andLike("name", name);
        }
        return mallGoodsMapper.selectCountByExample(example);
    }


    @Override
    public int createGoods(MallGoods mallGoods) {
        return mallGoodsMapper.insertSelective(mallGoods);
    }


    @Override
    public int updateGoods(MallGoods mallGoods) {
        Example example = new Example(MallGoods.class);
        example.createCriteria().andEqualTo("id", mallGoods.getId());
        return mallGoodsMapper.updateByExampleSelective(mallGoods, example);
    }


    @Override
    public int deleteGoods(Integer id) {
        Example example = new Example(MallGoods.class);
        example.createCriteria().andEqualTo("id", id);
        return mallGoodsMapper.deleteByExample(example);
    }
}
