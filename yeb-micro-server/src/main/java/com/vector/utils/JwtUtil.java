package com.vector.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL = 36 * 60 * 1000L;// 36 * 60 *1000  36分钟
    //设置秘钥明文
    public static final String JWT_KEY = "admin";
    // JWT存储的请求头
    public static final String TOKEN_AUTHORIZATION = "Authorization";
    // JWT 负载中拿到开头
    public static final String TOKEN_BEARER = "Bearer";


    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 加密后的秘钥 secretKey
     *
     * @return
     */
    public static SecretKey generateKey() {
        byte[] encodedKey = Base64.getEncoder().encode(JwtUtil.JWT_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("yuanjie")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(SignatureAlgorithm.HS256, generateKey()) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 生成jwt
     *
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 生成jwt
     *
     * @param subject   token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    /**
     * 创建token
     *
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    /**
     * 解析获取荷载
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        return Jwts.parser()
                .setSigningKey(generateKey())
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 从token获取用户名
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static String getUserNameFromToken(String jwt) throws Exception {
        return parseJWT(jwt).getSubject();
    }

    /**
     * 验证token是否有效
     *
     * @param
     * @throws Exception
     */
    public static boolean validateToken(String token, UserDetails userDetails) throws Exception {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否失效
     *
     * @param
     * @throws Exception
     */
    public static boolean isTokenExpired(String token) throws Exception {
        Date expired = getExpiredDateFromToken(token);
        return expired.before(new Date());
    }

    /**
     * 从token中获取时间
     *
     * @param
     * @throws Exception
     */
    public static Date getExpiredDateFromToken(String token) throws Exception {
        Claims claims = parseJWT(token);
        return claims.getExpiration();
    }

    /**
     * 验证token是否可以被刷新
     *
     * @param
     * @throws Exception
     */
    public boolean canRefresh(String token) throws Exception {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) throws Exception {
        return createJWT(getUserNameFromToken(token), JWT_TTL);
    }

    public static void main(String[] args) throws Exception {
        String jwt = JwtUtil.createJWT("123");
        System.out.println("加密后" + jwt);
        Claims claims = JwtUtil.parseJWT(jwt);
        String subject = claims.getSubject();
        System.out.println("解密后" + subject);
        System.out.println("有效时间:" + claims.getExpiration());
    }
}
