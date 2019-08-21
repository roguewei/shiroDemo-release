package com.winston.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.winston.entity.User;
import com.winston.properties.SecurityProperties;
import com.winston.service.IUserService;
import com.winston.service.IWechatService;
import com.winston.utils.result.CodeMsg;
import com.winston.utils.result.Result;
import com.winston.utils.wechat.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Winston
 * @title: WechatServiceImpl
 * @projectName shiro-parent
 * @description:
 * @date 2019/7/20 14:25
 */
@Service
@Slf4j
public class WechatServiceImpl implements IWechatService {

    // 调用公众号接口获取TOKEN
    private static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    // 获取消息模板id
    private static String TEMPLATE_ID_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKE";

    // 发送模板消息
    private static String SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private IUserService userService;

    @Override
    public String getAccessToken() {
        try {
            String appId = securityProperties.getWechat().getAppId();
            String secret = securityProperties.getWechat().getSecret();
            ACCESS_TOKEN_URL = ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", secret);
            JSONObject jsonObject = NetUtil.doGetJson(ACCESS_TOKEN_URL);
            if(jsonObject.getString("access_token") != null){
                String accessToken = jsonObject.getString("access_token");
                log.info("获取的access_token是： ---------- "+accessToken);
                return accessToken;
            }
            return JSON.toJSONString(Result.error(CodeMsg.WECHAT_GET_ACCESSTOKEN_ERROR));
        }catch (Exception e){
            return JSON.toJSONString(Result.error(CodeMsg.WECHAT_GET_ACCESSTOKEN_ERROR));
        }
    }

    @Override
    public String getTemplateId() {
        try {
            String accessToken = getAccessToken();
            Map<String, String> data = new HashMap<>();
            data.put("template_id_short", "TM00201");
            TEMPLATE_ID_URL = TEMPLATE_ID_URL.replace("ACCESS_TOKE", accessToken);
            JSONObject post = NetUtil.post(TEMPLATE_ID_URL, data);
            if(post != null){
                if("ok".equals(post.getString("errmsg"))){
                    String template_id = post.getString("template_id");
                    log.info("获取的template_id是： ---------- "+template_id);
                    return template_id;
                }else{
                    return JSON.toJSONString(Result.error(CodeMsg.WECHAT_GET_TEMPLATEID_ERROR));
                }
            }
        }catch (Exception e){
            return JSON.toJSONString(Result.error(CodeMsg.WECHAT_GET_TEMPLATEID_ERROR));
        }
        return null;
    }

    @Override
    public void sendMessageByWechat(List<String> openIds, String title, String alarmDescriptions, String sendDateTime){
        StringBuffer resBuff = new StringBuffer();
//            String templateId = getTemplateId();

        String accessToken = getAccessToken();
        SEND_TEMPLATE_URL = SEND_TEMPLATE_URL.replace("ACCESS_TOKEN", accessToken);
        //封装请求体
        MsgTemplate template = new MsgTemplate();
        template.setTemplateId("cuZfrFnND6K7USIYrwZhHJRp9HDRyHOUBRkBglgGB5g");
        template.setUrl("http://weixin.qq.com/download");
        List<TemplateParam> templateParams = new ArrayList<>();
        String[] failures = alarmDescriptions.split(",,");
        String alarmDescStr = "\\r\\n";
        if (failures != null && failures.length > 0) {
            for (String failure : failures) {
                alarmDescStr += failure + "\\r\\n";
            }
            //去掉最后的换行符号
            alarmDescStr = alarmDescStr.substring(0, alarmDescStr.lastIndexOf("\\r\\n"));
        }
        String allStr = title + alarmDescStr;
        while (allStr.length() > 49) {
            alarmDescStr = alarmDescStr.substring(0, alarmDescStr.lastIndexOf("\\r\\n"))+"...";
            allStr = title + alarmDescStr;
        }
        TemplateParam first = new TemplateParam("first", title, "#DB1A1B");
        TemplateParam keyword1 = new TemplateParam("keyword1", "美少男Winston" + "", "#DB1A1B");
        TemplateParam keyword2 = new TemplateParam("keyword2", alarmDescStr + "", "#DB1A1B");
        TemplateParam keyword3 = new TemplateParam("keyword3", sendDateTime, "#DB1A1B");
        TemplateParam remark = new TemplateParam("remark", "有问题找我啊，我又不会给你解决", "#DB1A1B");
        templateParams.add(first);
        templateParams.add(keyword1);
        templateParams.add(keyword2);
        templateParams.add(keyword3);
        templateParams.add(remark);
        template.setTemplateParamList(templateParams);
        if (openIds.size() > 0) {
            for (String openID : openIds) {
                template.setToUser(openID);
                String paramStr = template.toJSON();
                String resJson = NetUtil.doTemplateMsgPost(SEND_TEMPLATE_URL, paramStr);
                JSONObject object = JSON.parseObject(resJson);
                if("ok".equals(object.getString("errmsg"))){
                    log.info("发送模板消息成功================");
                }
            }
        }
    }

    @Override
    public String authorize() {
        String appId = securityProperties.getWechat().getAppId();
        String uri = URLEncoder.encode(securityProperties.getWechat().getRedirectUrl());
        // 拼接请求连接URL
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId
                + "&redirect_uri="+uri
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE#wechat_redirect";
    }

    @Override
    public User weixinLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String appId = securityProperties.getWechat().getAppId();
        String secret = securityProperties.getWechat().getSecret();

        // 用户同意授权后，能获取到code
        Map<String, String[]> params = request.getParameterMap();//针对get获取get参数
        String[] codes = params.get("code");//拿到code的值
        String code = codes[0];//code
        //String[] states = params.get("state");
        //String state = states[0];//state

        // 用户同意授权
        if (!"authdeny".equals(code)) {
            // 获取网页授权access_token
            Oauth2Token oauth2Token = getOauth2AccessToken(appId, secret, code);
            // 网页授权接口访问凭证
            String accessToken = oauth2Token.getAccessToken();
            // 用户标识
            String openId = oauth2Token.getOpenId();
            // 获取用户信息
            WeChatUser weChatUser = getSNSUserInfo(accessToken, openId);

            //具体业务start
            User user = userService.selectByOpenId(weChatUser.getOpenId());
            if(user == null){
                userService.save(weChatUser);
                user = userService.selectByOpenId(weChatUser.getOpenId());
            }
            return user;
        }else {
            return null;
        }
    }

    /**
     * 获取网页授权凭证
     *
     * @param appId 公众账号的唯一标识
     * @param appSecret 公众账号的密钥
     * @param code
     * @return WeixinAouth2Token
     */
    private static Oauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        Oauth2Token wat = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        requestUrl = requestUrl.replace("APPID", appId);
        requestUrl = requestUrl.replace("SECRET", appSecret);
        requestUrl = requestUrl.replace("CODE", code);
        // 获取网页授权凭证
        JSONObject jsonObject = JSON.parseObject(NetUtil.get(requestUrl));
        if (null != jsonObject) {
            try {
                wat = new Oauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInteger("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取网页授权凭证失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return wat;
    }

    /**
     * 通过网页授权获取用户信息
     *
     * @param accessToken 网页授权接口调用凭证
     * @param openId 用户标识
     * @return SNSUserInfo
     */
    private static WeChatUser getSNSUserInfo(String accessToken, String openId) throws IOException {
        WeChatUser weChatUser = null;
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 通过网页授权获取用户信息
//        JSONObject jsonObject =  JSON.parseObject(NetUtil.get(requestUrl));
        JSONObject jsonObject =   NetUtil.doGetJson(requestUrl);
        if (null != jsonObject) {
            try {
                weChatUser = new WeChatUser();
                // 用户的标识
                weChatUser.setOpenId(jsonObject.getString("openid"));
                // 昵称
                weChatUser.setNickname(jsonObject.getString("nickname"));
                // 性别（1是男性，2是女性，0是未知）
                weChatUser.setSex(jsonObject.getInteger("sex"));
                // 用户所在国家
                weChatUser.setCountry(jsonObject.getString("country"));
                // 用户所在省份
                weChatUser.setProvince(jsonObject.getString("province"));
                // 用户所在城市
                weChatUser.setCity(jsonObject.getString("city"));
                // 用户头像
                weChatUser.setHeadImgUrl(jsonObject.getString("headimgurl"));
                // 用户特权信息
                List<String> list = JSON.parseArray(jsonObject.getString("privilege"),String.class);
                weChatUser.setPrivilegeList(list);
                //与开放平台共用的唯一标识，只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
                weChatUser.setUnionid(jsonObject.getString("unionid"));
            } catch (Exception e) {
                weChatUser = null;
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取用户信息失败 errcode:{} errmsg:{}", errorCode, errorMsg);
            }
        }
        return weChatUser;
    }
}
