package team.fjut.cf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.fjut.cf.mapper.UserCustomInfoMapper;
import team.fjut.cf.mapper.UserSealMapper;
import team.fjut.cf.mapper.UserTitleMapper;
import team.fjut.cf.pojo.po.UserCustomInfo;
import team.fjut.cf.pojo.po.UserSeal;
import team.fjut.cf.pojo.po.UserTitle;
import team.fjut.cf.pojo.vo.UserCustomInfoVO;
import team.fjut.cf.service.UserCustomInfoService;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @author axiang [2020/3/23]
 */
@Service
public class UserCustomInfoServiceImpl implements UserCustomInfoService {
    @Resource
    UserCustomInfoMapper userCustomInfoMapper;

    @Resource
    UserTitleMapper userTitleMapper;

    @Resource
    UserSealMapper userSealMapper;

    @Override
    public UserCustomInfoVO select(String username) {
        UserCustomInfoVO result = new UserCustomInfoVO();
        Example example = new Example(UserCustomInfo.class);
        example.createCriteria().andEqualTo("username", username);
        UserCustomInfo userCustomInfo = userCustomInfoMapper.selectOneByExample(example);
        // 如果为空返回空内容
        if (null == userCustomInfo) {
            return result;
        }
        result.setNickname(userCustomInfo.getNickname());
        result.setId(userCustomInfo.getId());
        result.setUsername(userCustomInfo.getUsername());
        result.setAvatarUrl(userCustomInfo.getAvatarUrl());
        if (userCustomInfo.getAdjectiveId() != null) {
            UserTitle userAdjTitle = userTitleMapper.selectByPrimaryKey(userCustomInfo.getAdjectiveId());
            result.setAdjectiveTitle(userAdjTitle.getName());
        }
        if (userCustomInfo.getArticleId() != null) {
            UserTitle userArtTitle = userTitleMapper.selectByPrimaryKey(userCustomInfo.getArticleId());
            result.setArticleTitle(userArtTitle.getName());
        }
        if (userCustomInfo.getSealId() != null) {
            UserSeal userSeal = userSealMapper.selectByPrimaryKey(userCustomInfo.getSealId());
            result.setSealUrl(userSeal.getPictureUrl());
        }
        return result;
    }
}
