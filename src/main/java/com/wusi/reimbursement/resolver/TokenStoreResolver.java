package com.wusi.reimbursement.resolver;


import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.JwtUtil;
import com.wusi.reimbursement.utils.RedisUtil;

import java.util.Date;


public interface TokenStoreResolver {
    String KEYPREFIX = "JwtToken:";

    Long delaysAllowed = 100L;

    Long times = 1000 * 60 *60 *4L;

    /**
     * token存储
     *
     * @param token
     * @param userId
     * @param expireTime
     */
    default void addOrUpdateToken(String token, String userId, Date expireTime) {
        //System.out.println(token);
        if (expireTime == null) {
            RedisUtil.set(KEYPREFIX + token, userId, times);
        } else {
            Long ttl = expireTime.getTime() - System.currentTimeMillis();
            if (ttl > delaysAllowed) {
                RedisUtil.set(KEYPREFIX + token, userId, ttl);
            } else {
                throw new JwtUtil.TokenValidationException("token已过期");
            }
        }
    }

    /**
     * 校验token是否还能使用，包括校验token的有效时间
     *
     * @param token
     * @return
     */
    default Boolean validateToken(String token) {
        Object object = RedisUtil.get(KEYPREFIX + token);
        String userId = object == null ? null : (String) object;
        if (DataUtil.isNotEmpty(userId)) {
            return true;
        } else {
            throw new JwtUtil.TokenValidationException("token已过期");
        }
    }

    /**
     * 删除token,使token不能再使用
     *
     * @param token
     */
    default void deleteTokenByToken(String token) {
        RedisUtil.del(KEYPREFIX + token);
    }
}
