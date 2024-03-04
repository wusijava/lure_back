package com.wusi.reimbursement.redis;


import com.wusi.reimbursement.utils.PropertiesUtil;
import com.wusi.reimbursement.utils.SerializeUtil;

public abstract class CacheManager<K,V> {

    private static final String importantprefix = null;

    private static Boolean cacheable = PropertiesUtil.getBoolean("redis.cacheable",true);
    public abstract int getDbIndex();

    public abstract JedisManager getJedisManager();

    /**
     * 添加缓存
     * @param key
     * @param value
     * @param expireTime -1:永不销毁
     */
    protected void setKeyValue(K key,V value,int expireTime ){
        if(cacheable) {
            int dbIndex = getDbIndex();
            try {
                if(key instanceof String && importantprefix !=null){
                    getJedisManager().saveValueByKey(dbIndex, SerializeUtil.serialize(importantprefix+key), SerializeUtil.serialize(value), expireTime);
                }else {
                    getJedisManager().saveValueByKey(dbIndex, SerializeUtil.serialize(key), SerializeUtil.serialize(value), expireTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除缓存
     * @param key
     */
    protected void deleteByKey(K key){
        int dbIndex = getDbIndex();
        try {
            if(key instanceof String && importantprefix !=null){
                getJedisManager().deleteByKey(dbIndex,SerializeUtil.serialize(importantprefix+key));
            }else{
                getJedisManager().deleteByKey(dbIndex,SerializeUtil.serialize(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected V getByKey(K key){
        if(!cacheable) {
            return null;
        }
        int dbIndex = getDbIndex();
        byte[] byteKey ;
        if( key instanceof String && importantprefix !=null){
            byteKey = SerializeUtil.serialize(importantprefix+key);
        }else{
            byteKey = SerializeUtil.serialize(key);
        }
        byte[] byteValue = new byte[0];
        try {
            byteValue = getJedisManager().getValueByKey(dbIndex, byteKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (V) SerializeUtil.deserialize(byteValue);

    }



}
