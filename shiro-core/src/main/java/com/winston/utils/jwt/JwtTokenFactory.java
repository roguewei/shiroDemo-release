package com.winston.utils.jwt;


import com.winston.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class JwtTokenFactory {
    @Resource
    private SecurityProperties securityProperties;

    /**
     * 构建jwtToken
     * @return JwtToken
     */
    public JwtToken createJwtToken(Claims claims) {

        // 生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 设置过期时间
        Date exp = new Date(nowMillis+securityProperties.getJwt().getTokenExpirationTime());
        // 构建token: 用户信息, 发行者, 发行时间, 过期时间, 发行密钥
        String token = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // issuer：jwt签发人
                .setIssuer(securityProperties.getJwt().getTokenIssuer())
                // iat: jwt的签发时间
                .setIssuedAt(now)
                // 设置过期时间
                .setExpiration(exp)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(SignatureAlgorithm.HS512, securityProperties.getJwt().getTokenSigningKey())
                //开始压缩为xxxxxxxxxxxxxx.xxxxxxxxxxxxxxx.xxxxxxxxxxxxx这样的jwt
                .compact();
        return new RawAccessJwtToken(token, claims);
    }

}