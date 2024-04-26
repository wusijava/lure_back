package com.wusi.reimbursement.wx;

import com.wusi.reimbursement.redis.CacheManager;
import com.wusi.reimbursement.redis.JedisManager;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by YL on 2018/1/12.
 */
@Component
public class WxCache {


    private final String SERVICE_PREFIX = "wx-cache-";



    public void saveAccessToken(String appId,String accessToken) {
        if (DataUtil.isEmpty(appId) || DataUtil.isEmpty(accessToken)){
            return;
        }
        RedisUtil.set(SERVICE_PREFIX + appId +"-accessToken", accessToken, 6500);
    }

    public String getAccessToken(String appId) {
         Object obj = RedisUtil.get(SERVICE_PREFIX + appId +"-accessToken");
         return obj == null ? null : obj.toString();
    }

    public void removeAccessToken(String appId) {
        RedisUtil.del(SERVICE_PREFIX + appId +"-accessToken");
    }
}
