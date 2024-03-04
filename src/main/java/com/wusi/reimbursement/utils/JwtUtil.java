package com.wusi.reimbursement.utils;


import com.wusi.reimbursement.resolver.TokenStoreResolver;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;


@Slf4j
public class JwtUtil {


    /**
     * JWT 加解密类型
     */
    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;
    /**
     * JWT 生成密钥使用的密码
     */
    private static String JWT_RULE = "wusi";

    /**
     * JWT 添加至HTTP HEAD中的前缀
     */
    private static final String JWT_SEPARATOR = "Bearer ";

    private static final String HEADER_STRING = "Authorization";

    public static void setJwtRule(String rule) {
        if (DataUtil.isNotEmpty(rule)) {
            JWT_RULE = rule;
        }
    }

    /**
     * 使用JWT默认方式，生成加解密密钥
     *
     * @param alg 加解密类型
     * @return
     */
    public static SecretKey generateKey(SignatureAlgorithm alg) {
        return MacProvider.generateKey(alg);
    }

    /**
     * 使用指定密钥生成规则，生成JWT加解密密钥
     *
     * @param alg  加解密类型
     * @param rule 密钥生成规则
     * @return
     */
    public static SecretKey generateKey(SignatureAlgorithm alg, String rule) {
        // 将密钥生成键转换为字节数组
        byte[] bytes = Base64.decodeBase64(rule);
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, alg.getJcaName());
    }

    /**
     * 构建JWT
     *
     * @param alg      jwt 加密算法
     * @param key      jwt 加密密钥
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param jti      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     */
    public static String buildJWT(SignatureAlgorithm alg, Key key, String sub, String aud, String jti, String iss, Date nbf, Integer duration, Map<String, Object> claims) {
        // jwt的签发时间
        DateTime iat = DateTime.now();
        // jwt的过期时间，这个过期时间必须要大于签发时间
        DateTime exp = null;
        if (duration != null) {
            exp = (nbf == null ? iat.plusSeconds(duration) : new DateTime(nbf).plusSeconds(duration));
        }

        // 获取JWT字符串
        String compact = Jwts.builder()
                .signWith(alg, key)
                .setSubject(sub)
                .setAudience(aud)
                .setClaims(claims)
                .setId(jti)
                .setIssuer(iss)
                .setNotBefore(nbf)
                .setIssuedAt(iat.toDate())
                .setExpiration(exp != null ? exp.toDate() : null)
                .compact();

        // 在JWT字符串前添加"Bearer "字符串，用于加入"Authorization"请求头
        return JWT_SEPARATOR + compact;
    }

    /**
     * 构建JWT
     *
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param jti      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     */
    public static String buildJWT(String sub, String aud, String jti, String iss, Date nbf, Integer duration) {
        return buildJWT(JWT_ALG, generateKey(JWT_ALG, JWT_RULE), sub, aud, jti, iss, nbf, duration, null);
    }

    /**
     * 构建JWT
     *
     * @param sub jwt 面向的用户
     * @param jti jwt 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @return JWT字符串
     */
    public static String buildJWT(String sub, String jti, Integer duration) {
        return buildJWT(sub, null, jti, null, null, duration);
    }

    /**
     * 构建JWT
     *
     * @return JWT字符串
     */
    public static String buildJWT(Map<String, Object> claims, Integer duration) {
        return buildJWT(JWT_ALG, null, UUID.randomUUID().toString(), null, null, null, null, duration, claims);
    }

    /**
     * 构建JWT
     * <p>使用 UUID 作为 jti 唯一身份标识</p>
     * <p>JWT有效时间 600 秒，即 10 分钟</p>
     *
     * @param sub jwt 面向的用户
     * @return JWT字符串
     */
    public static String buildJWT(String sub) {
        return buildJWT(sub, null, UUID.randomUUID().toString(), null, null, 600);
    }

    /**
     * 解析JWT
     *
     * @param key       jwt 加密密钥
     * @param claimsJws jwt 内容文本
     * @return {@link Jws}
     * @throws Exception
     */
    public static Jws<Claims> parseJWT(Key key, String claimsJws) {
        // 移除 JWT 前的"Bearer "字符串
        claimsJws = StringUtils.substringAfter(claimsJws, JWT_SEPARATOR);
        // 解析 JWT 字符串
        return Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
    }

    /**
     * 解析JWT
     *
     * @param claimsJws jwt 内容文本
     * @return {@link Jws}
     * @throws Exception
     */
    public static Map<String, Object> parseJWT(String claimsJws) {
        Key key = generateKey(JWT_ALG, JWT_RULE);
        // 解析 JWT 字符串
        Jws<Claims> claims = parseJWT(key, claimsJws);
        return claims.getBody();
    }

    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @return ture or false
     */
    public static Boolean checkJWT(String claimsJws) {
        boolean flag = false;
        try {
            SecretKey key = generateKey(JWT_ALG, JWT_RULE);
            // 获取 JWT 的 payload 部分
            flag = (parseJWT(key, claimsJws).getBody() != null);
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
        }
        return flag;
    }

    /**
     * 校验JWT
     *
     * @param key       jwt 加密密钥
     * @param claimsJws jwt 内容文本
     * @param sub       jwt 面向的用户
     * @return ture or false
     */
    public static Boolean checkJWT(Key key, String claimsJws, String sub) {
        boolean flag = false;
        try {
            // 获取 JWT 的 payload 部分
            Claims claims = parseJWT(key, claimsJws).getBody();
            // 比对JWT中的 sub 字段
            flag = claims.getSubject().equals(sub);
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
        }
        return flag;
    }

    /**
     * 校验JWT
     *
     * @param claimsJws jwt 内容文本
     * @param sub       jwt 面向的用户
     * @return ture or false
     */
    public static Boolean checkJWT(String claimsJws, String sub) {
        return checkJWT(generateKey(JWT_ALG, JWT_RULE), claimsJws, sub);
    }

    public static final String USER_NAME = "UserName";

    public static final String USER_ID = "UserId";

    public static final long EXPIRATION_TIME = 30 * 20 * 60 * 10000L;

    /**
     * @param userId
     * @param userName
     * @param tokenStoreResolver
     * */
    public static String generateToken(String userId, String userName, TokenStoreResolver tokenStoreResolver) {
        HashMap<String, Object> map = new HashMap<>(8);
        map.put(USER_NAME, userName);
        map.put(USER_ID, userId);
        Date expire = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(expire)
                .signWith(JWT_ALG, generateKey(JWT_ALG, JWT_RULE))
                .compact();
        String token = JWT_SEPARATOR + jwt;
        if (DataUtil.isNotEmpty(tokenStoreResolver)) {
            tokenStoreResolver.addOrUpdateToken(token, userId, expire);
        }
        return token;
    }

    public static void logoutToken(HttpServletRequest request, TokenStoreResolver tokenStoreResolver) {
        String token = request.getHeader(HEADER_STRING);
        if (DataUtil.isNotEmpty(token) && DataUtil.isNotEmpty(tokenStoreResolver)) {
            tokenStoreResolver.deleteTokenByToken(token);
        }
    }

    public static HttpServletRequest validateTokenAndAddUserIdToHeader(HttpServletRequest request, TokenStoreResolver tokenStoreResolver) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            if (DataUtil.isNotEmpty(tokenStoreResolver)) {
                tokenStoreResolver.validateToken(token);
            }
            try {
                Map<String, Object> body = Jwts.parser()
                        .setSigningKey(generateKey(JWT_ALG, JWT_RULE))
                        .parseClaimsJws(token.replace(JWT_SEPARATOR, ""))
                        .getBody();
                return new CustomHttpServletRequest(request, body);
            } catch (Exception e) {
                log.info(e.getMessage());
                throw new TokenValidationException(e.getMessage());
            }
        } else {
            throw new TokenValidationException("Missing token");
        }
    }

    public static class CustomHttpServletRequest extends HttpServletRequestWrapper {
        private Map<String, String> claims;

        public CustomHttpServletRequest(HttpServletRequest request, Map<String, ?> claims) {
            super(request);
            this.claims = new HashMap<>();
            claims.forEach((k, v) -> this.claims.put(k, String.valueOf(v)));
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (claims != null && claims.containsKey(name)) {
                return Collections.enumeration(Arrays.asList(claims.get(name)));
            }
            return super.getHeaders(name);
        }

        public Map<String, String> getClaims() {
            return claims;
        }
    }

    public static class TokenValidationException extends RuntimeException {
        public TokenValidationException(String msg) {
            super(msg);
        }
    }

    public static String getCurrentUserId(HttpServletRequest request) {
        try {
            CustomHttpServletRequest jwtRequest = (CustomHttpServletRequest) request;
            return jwtRequest.getClaims().get(JwtUtil.USER_ID);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentUserName(HttpServletRequest request) {
        try {
            CustomHttpServletRequest jwtRequest = (CustomHttpServletRequest) request;
            return jwtRequest.getClaims().get(JwtUtil.USER_NAME);
        } catch (Exception e) {
            return null;
        }
    }


    public static String getCurrentToken(HttpServletRequest request) {
        try {
            return request.getHeader(HEADER_STRING);
        } catch (Exception e) {
            return null;
        }
    }

    public static void refreshAndAddTokenToResponseHeader(HttpServletRequest request, HttpServletResponse response, String userId, String userName, TokenStoreResolver tokenStoreResolver) {
        try {
            String jwtToken = generateToken(userId, userName, tokenStoreResolver);
            response.setHeader(HEADER_STRING, jwtToken);
        } catch (NoSuchBeanDefinitionException e) {
            throw e;
        }
    }

}
