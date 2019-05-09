package com.gerry.yitao.common.util;

import com.gerry.yitao.common.entity.JwtConstants;
import com.gerry.yitao.common.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/6 19:04
 * @Description:
 */
public class JwtUtils {
    /**
     * 生成Token
     * @param userInfo
     * @param privateKey
     * @param expireMinutes
     * @return
     */
    public static String generateToken(UserInfo userInfo, PrivateKey privateKey, int expireMinutes) {
        return Jwts.builder()
                .claim(JwtConstants.JWT_KEY_ID, userInfo.getId())
                .claim(JwtConstants.JWT_KEY_USER_NAME, userInfo.getUsername())
                .setExpiration(DateTime.now().plusMinutes(expireMinutes).toDate())
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    /**
     * 公钥解析Token
     * @param publicKey
     * @param token
     * @return
     */
    public static Jws<Claims> parseToken(PublicKey publicKey, String token) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }


    /**
     * 从Token中获取用户信息（使用公钥解析）
     * @param publicKey
     * @param token
     * @return
     */
    public static UserInfo getUserInfo(PublicKey publicKey, String token) {
        Jws<Claims> claimsJws = parseToken(publicKey, token);
        Claims body = claimsJws.getBody();
        return new UserInfo(
                ObjectUtils.toLong(body.get(JwtConstants.JWT_KEY_ID)),
                ObjectUtils.toString(body.get(JwtConstants.JWT_KEY_USER_NAME))
        );
    }
}
