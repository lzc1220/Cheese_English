package com.example.zhishibeici.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtils {
    private String SECRET = "pptshuaishuai"; //密钥

    private final static Long access_token_expiration = 7200L;

    public String generateJwtToken(String openId, String session_key) {
        //设置token header
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256"); //algorithm
        map.put("typ", "JWT");   //type


        Map<String, Object> claims = new HashMap<>();
        claims.put("openid", openId);
        claims.put("session_key", session_key);

        return Jwts.builder()
                .setHeader(map)
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())

                .setIssuedAt(new Date())                                          //签发时间
                .setExpiration(new Date(System.currentTimeMillis() + access_token_expiration * 1000)) //过期时间
                .setSubject("user")                                               //JWT主体，即所有人

                //把header、claims分别编码拼接，构成签名
                .signWith(SignatureAlgorithm.HS256, SECRET)                       //签名
                .compact();                                                       //压缩生成token

    }

    public boolean checkToken(String token) {

        if (StringUtils.isEmpty(token)) return false;
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean checkToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            return checkToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public int getOpenIdFromToken(String token) {
        if (!checkToken(token)) {
            return -1;
        }

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (int) claims.get("openid");
    }

    public int getOpenIdFromToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return getOpenIdFromToken(token);

    }

}
