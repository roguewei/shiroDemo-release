package com.winston.utils.service;


import com.winston.constant.Commons;
import com.winston.entity.User;
import com.winston.properties.SecurityProperties;
import com.winston.utils.HttpUtil;
import com.winston.utils.jwt.JwtTokenFactory;
import com.winston.utils.jwt.RawAccessJwtToken;
import com.winston.utils.redis.TokenKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class TokenService {


    @Autowired
    private RawAccessJwtToken rawAccessJwtToken;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Autowired
    private SecurityProperties securityProperties;


    public Integer getIdByToken(HttpServletRequest request) {
        return rawAccessJwtToken.getUserId();
    }

    public Claims getClaims(HttpServletRequest request) {
        return rawAccessJwtToken.getClaims();
    }

    public boolean authToken(HttpServletRequest httpServletRequest) {

        HttpSession session = httpServletRequest.getSession();
        if(session == null){
            return false;
        }
        String headerToken = httpServletRequest.getHeader(securityProperties.getJwt().getHeader());
        String sessionId = httpServletRequest.getSession().getId();
        if (sessionId != null) {
            //处理token，
            String token = redisService.get(TokenKey.getLoginToken, sessionId, String.class);
            if (StringUtils.isBlank(headerToken) || StringUtils.isBlank(token)) {
                return false;
            }else{
                if(!headerToken.equals(token)){
                    return false;
                }
                //redis处理token，重置过期时间，用于可以在线时长
                redisService.retExpire(TokenKey.getLoginToken, sessionId,(int)securityProperties.getJwt().getTokenExpirationTime()/1000 , token);
                return true;
            }
        } else {
            return false;
        }
    }

    /***
     * 构建token
     * @param user
     * @return
     */
    public String getToken(User user) {
        HttpServletRequest request = HttpUtil.getRequest();
        String sessionId = request.getSession().getId();
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        claims.put(Commons.USER_ID, user.getId());
        claims.put("username", user.getUsername());

        String jwtToken = jwtTokenFactory.createJwtToken(claims).getToken(); // 构建token
        redisService.set(TokenKey.getLoginToken, sessionId, jwtToken);
        return jwtToken;
    }

    public void clearToken() {
        String sessionId = getSessionId();
        redisService.del(TokenKey.getLoginToken, sessionId);
    }

    public void clearSessionKey(String key) {
        redisService.del("shiro:session:" + key);
    }

    private String getSessionId(){
        HttpServletRequest request = HttpUtil.getRequest();
        String sessionId = request.getSession().getId();
        return sessionId;
    }

}
