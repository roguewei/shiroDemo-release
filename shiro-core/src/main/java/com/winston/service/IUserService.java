package com.winston.service;


import com.winston.entity.User;
import com.winston.utils.wechat.WeChatUser;

import java.util.List;

public interface IUserService {

    List<User> queryAll();

    User queryByUser(User user);

    User selectByUsername(String username);

    User selectByOpenId(String openId);

    int save(WeChatUser weChatUser);

    int save(User user);

}
