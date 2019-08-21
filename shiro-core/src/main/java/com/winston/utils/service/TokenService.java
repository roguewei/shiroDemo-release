package com.winston.utils.service;


import com.winston.constant.Commons;
import com.winston.entity.User;
import com.winston.properties.SecurityProperties;
import com.winston.utils.jwt.JwtTokenFactory;
import com.winston.utils.jwt.RawAccessJwtToken;
import com.winston.utils.redis.TokenKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
//        String key = session.getId();
        Integer userId = getIdByToken(httpServletRequest);
        if (userId != null) {
            //处理token，
            String token = redisService.get(TokenKey.getLoginToken, String.valueOf(userId), String.class);
            if (!rawAccessJwtToken.getToken().equals(token)) {
                return false;
            }
            //redis处理token，重置过期时间，用于可以在线时长
            redisService.retExpire(TokenKey.getLoginToken, String.valueOf(userId),(int)securityProperties.getJwt().getTokenExpirationTime()/1000 , token);
            return true;
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
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        claims.put(Commons.USER_ID, user.getId());
        claims.put("username", user.getUsername());

        String jwtToken = jwtTokenFactory.createJwtToken(claims).getToken(); // 构建token
        redisService.set(TokenKey.getLoginToken, String.valueOf(user.getId()), jwtToken);
        return jwtToken;
    }

    public void clearToken(String key) {
        redisService.del(TokenKey.getLoginToken, key);
    }

    public void clearSessionKey(String key) {
        redisService.del("shiro:session:" + key);
    }

}
