package com.winston.utils.jwt;


import com.winston.constant.Commons;
import com.winston.properties.SecurityProperties;
import com.winston.utils.HttpUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Data
@Component
public class RawAccessJwtToken implements JwtToken {

    @Autowired
    private SecurityProperties securityProperties;

    private String token;


    private Claims claims;

    public RawAccessJwtToken(){

    }

    public RawAccessJwtToken(String token) {
        this.token = token;
    }

    public RawAccessJwtToken(String token, Claims claims) {
        this.token = token;
        this.claims = claims;
    }
    public RawAccessJwtToken(HttpServletRequest request){
        this.token = request.getHeader(securityProperties.getJwt().getHeader());
    }

    public void setRequest(HttpServletRequest request) {
        this.token = request.getHeader(securityProperties.getJwt().getHeader());
    }

    /**
     * 用签名解析token
     * @param signingKey
     * @return
     */
    public Jws<Claims> parseClaims(String signingKey) {
        if(StringUtils.isNotBlank(signingKey) && StringUtils.isNotBlank(this.token)) {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws((this.token).trim());
            return claimsJws;
        }
        return null;

    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public Claims getClaims() {
        setRequest(HttpUtil.getRequest());
        Jws<Claims> claimsJws = parseClaims(securityProperties.getJwt().getTokenSigningKey());
        return claimsJws.getBody();
    }

    @Override
    public Integer getUserId() {
        setRequest(HttpUtil.getRequest());
        Claims claims = getClaims();
        return claims.get(Commons.USER_ID, Integer.class);
    }

    @Override
    public String getUserName() {
        setRequest(HttpUtil.getRequest());
        Claims claims = getClaims();
        return claims.get("username", String.class);
    }
}
