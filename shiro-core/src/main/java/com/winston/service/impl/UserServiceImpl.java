package com.winston.service.impl;

import com.winston.entity.User;
import com.winston.entity.UserExample;
import com.winston.mapper.UserMapper;
import com.winston.service.IUserService;
import com.winston.utils.jwt.RawAccessJwtToken;
import com.winston.utils.shiro.PasswordHelper;
import com.winston.utils.wechat.WeChatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Winston
 * @title: UserServiceImpl
 * @projectName shiroDemo
 * @description:
 * @date 2019/7/24 14:25
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private RawAccessJwtToken rawAccessJwtToken;

    @Override
    public List<User> queryAll() {
        return mapper.selectByExample(new UserExample());
    }

    @Override
    public User queryByUser(User user) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if(user.getId() != null){
            criteria.andIdEqualTo(user.getId());
        }
        if(user.getUsername() != null){
            criteria.andUsernameEqualTo(user.getUsername());
        }
        if(user.getOpenId() != null){
            criteria.andOpenIdEqualTo(user.getOpenId());
        }
        List<User> users = mapper.selectByExample(example);
        if(users != null && users.size() > 0){
            return users.get(0);
        }
        return null;
    }

    @Override
    public User selectByUsername(String username) {
        return null;
    }

    @Override
    public User selectByOpenId(String openId) {
        return null;
    }



    @Override
    public int save(WeChatUser weChatUser) {
        String nicname = weChatUser.getNickname();
        long nowTime = new Date().getTime();

        User user = new User();
        user.setOpenId(weChatUser.getOpenId());
        user.setOpenidHex(weChatUser.getOpenId());
        user.setEnable(1);
        user.setSex(String.valueOf(weChatUser.getSex()));
        user.setNickName(weChatUser.getNickname());
        user.setState("1");
        user.setCreateOpr(nicname);
        user.setCreateTime(nowTime);
        user.setUpdateOpr(nicname);
        user.setUpdateTime(nowTime);
        user.setOperatorType("1");
        PasswordHelper passwordHelper = new PasswordHelper();
        passwordHelper.encryptPasswordWx(user);
        return save(user);
    }

    @Override
    public int save(User user) {
        String nicname = rawAccessJwtToken.getUserName();
        long nowTime = new Date().getTime();

        user.setEnable(1);
        user.setState("1");
        user.setCreateOpr(nicname);
        user.setCreateTime(nowTime);
        user.setUpdateOpr(nicname);
        user.setUpdateTime(nowTime);
        user.setOperatorType("0");
        PasswordHelper passwordHelper = new PasswordHelper();
        passwordHelper.encryptPassword(user);
        mapper.insert(user);
        return user.getId();
    }

    //    @Override
//    public User queryByOpenId(String openId) {
//        UserExample example = new UserExample();
//        example.createCriteria().andOpenIdEqualTo(openId);
//        List<User> users = mapper.selectByExample(example);
//        return users.get(0);
//    }
}
