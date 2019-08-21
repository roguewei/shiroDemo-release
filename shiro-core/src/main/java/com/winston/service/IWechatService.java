package com.winston.service;


import com.winston.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IWechatService {

    String getAccessToken();

    String getTemplateId();

    void sendMessageByWechat(List<String> openIds, String title, String alarmDescriptions, String sendDateTime);

    public String authorize();

    public User weixinLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
