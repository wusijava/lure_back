/*
package com.wusi.reimbursement.service.impl;

import com.anji.captcha.service.CaptchaCacheService;
import com.wusi.reimbursement.redis.GlobalCacheManager;
import com.wusi.reimbursement.utils.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CaptchaServiceRedisImpl implements CaptchaCacheService {

    @Autowired
    private GlobalCacheManager cacheManager;


    @Override
    public void set(String key, String value, long expiresInSeconds) {
        cacheManager.set(key,value,(int)expiresInSeconds);
    }

    @Override
    public boolean exists(String key) {
        String s = cacheManager.get(key);
        return DataUtil.isNotEmpty(s);
    }

    @Override
    public void delete(String key) {
        cacheManager.delete(key);
        cacheManager.delete(key+ "_HoldTime");
    }

    @Override
    public String get(String key) {
        String data = cacheManager.get(key);
        if (DataUtil.isNotEmpty(data)){
            return data;
        }
        return null;
    }

    @Override
    public String type() {
        return null;
    }
}
*/
