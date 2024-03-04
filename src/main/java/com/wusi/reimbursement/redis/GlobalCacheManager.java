/*
package com.wusi.reimbursement.redis;

import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;


@Component
@Slf4j
public class GlobalCacheManager extends CacheManager<String,Object> {


    @Autowired
    private JedisManager manager;

    @Override
    public int getDbIndex() {
        return 8;
    }

    private boolean isCache = false;

    @Override
    public JedisManager getJedisManager() {
        return manager;
    }

    public void setSmsKey(String mobile,String code){
        if (DataUtil.isEmpty(mobile) || DataUtil.isEmpty(code)){
            return;
        }
        setKeyValue("mobile:"+mobile,code,1800);
    }

    */
/**
     * 图形验证码三分钟有效。
     * *//*

    public void setCaptchaKey(String token,Integer status){
        if (DataUtil.isEmpty(status) || DataUtil.isEmpty(token)){
            return;
        }
        setKeyValue("captchaToken:"+token,status,180);
    }

    public Integer getCaptchaKey(String token){
        if (DataUtil.isEmpty(token)){
            return null;
        }
        Integer status= (Integer)super.getByKey("captchaToken:" + token);
        if (DataUtil.isEmpty(status)){
            return null;
        }else {
            return status;
        }
    }


    */
/**
     * 开户验证码一分钟内防止重复发送
     * *//*

    public void setSmsKey(String mobile){
        if (DataUtil.isEmpty(mobile) ){
            return;
        }
        setKeyValue("mobile:"+mobile,mobile,60);
    }

    public String getSmsKey(String mobile){
        if(DataUtil.isEmpty(mobile)){
            return null;
        }
        Object object = super.getByKey("mobile:" + mobile);
        if(DataUtil.isEmpty(object)){
            return null;
        }else{
            return object.toString();
        }
    }

    public void setRegisterCount(String ipAddress, Integer count) {
        if (DataUtil.isEmpty(ipAddress)) {
            return;
        }
        setKeyValue("registerCount:"
                + DateUtil.getDateString(new Date(), 3)
                + ":" + ipAddress, count,86400);
    }

    public Integer getRegisterCount(String ipAddress){
        if (DataUtil.isEmpty(ipAddress)) {
            return null;
        }
        Object object = super.getByKey("registerCount:"
                + DateUtil.getDateString(new Date(), 3)
                + ":" + ipAddress);
        if (DataUtil.isEmpty(object)) {
            return 0;
        } else {
            return Integer.parseInt(object.toString());
        }
    }

    public void setSendSmsCount(String ipAddress, Integer count) {
        if (DataUtil.isEmpty(ipAddress)) {
            return;
        }
        setKeyValue("registerSmsCount:"
                + DateUtil.getDateString(new Date(), 3)
                + ":" + ipAddress, count,86400);
    }

    public Integer getSendSmsCount(String ipAddress){
        try {
            if (DataUtil.isEmpty(ipAddress)) {
                return null;
            }
            Object object = super.getByKey("registerSmsCount:"
                    + DateUtil.getDateString(new Date(), 3)
                    + ":" + ipAddress);
            if (DataUtil.isEmpty(object)) {
                return 0;
            } else {
                return Integer.parseInt(object.toString());
            }
        } catch (NumberFormatException e) {
            log.error("获取缓存次数异常:{}", e);
            return null;
        }
    }



    public void setLoginToken(String token,String timestamp,String clientDigest,String type){
        if(DataUtil.isEmpty(token) || DataUtil.isEmpty(timestamp) || DataUtil.isEmpty(clientDigest) || DataUtil.isEmpty(type)){
            return;
        }
        setKeyValue("token:"+token,timestamp+","+clientDigest+","+type,-1);
    }

    public String getLotinToken(String token){
        if(DataUtil.isEmpty(token)){
            return null;
        }
        Object object = super.getByKey("token:"+token);
        if(DataUtil.isEmpty(object)){
            return null;
        }
        return object.toString();
    }

    public void clearLoginToken(String token){
        if(DataUtil.isEmpty(token)){
            return;
        }
        super.deleteByKey("token:"+token);
    }

    public void clearSmsKey(String mobile){
        if(DataUtil.isEmpty(mobile)){
            return;
        }
        super.deleteByKey("mobile:" + mobile);
    }



    public void clearRiskRules(Integer poslevel){
        if(DataUtil.isEmpty(poslevel)){
            return;
        }
        super.deleteByKey("RiskRules:" + poslevel);
    }
    */
/**
     * 将此次修改的用户id保存起来有效时间传入times
     * *//*

    public void setUserIdKey(String id,String nickname,Integer times){
        if (DataUtil.isEmpty(id) || DataUtil.isEmpty(nickname)){
            return;
        }
        setKeyValue("user_api_nickname_id:"+id,nickname,times);
    }

    public String getUserNicknameKey(String id){
        if(DataUtil.isEmpty(id)){
            return null;
        }
        Object object = super.getByKey("user_api_nickname_id:" + id);
        if(DataUtil.isEmpty(object)){
            return null;
        }else{
            return (String) object;
        }
    }



    */
/**
     * 修改密码-验证过验证码
     *//*

    public void setTestCode(String mobile,String status){
        if(DataUtil.isEmpty(mobile) || DataUtil.isEmpty(status)){
            return;
        }
        setKeyValue("testCode:" + mobile,status,600);
    }

    public  String getTestCode(String mobile){
        if (DataUtil.isEmpty(mobile)){
            return null;
        }
        Object object = super.getByKey("testCode:" + mobile);
        if(DataUtil.isEmpty(object)){
            return null;
        }else{
            return object.toString();
        }
    }

    public Integer getRateIp(String ip){
        return (Integer) getByKey("IPrateLimit:"+ip);

    }

    public void setRateLimitIp(String ip){
        Integer limits = getRateIp(ip);
        if(limits==null){
            limits = 0;
        }
        limits++;
        setKeyValue("IPrateLimit:"+ip,limits,60);
    }

    public Integer getVoicesWitch(String userid){
        return (Integer) getByKey("VoicesWitch:"+userid);

    }

    public void setVoicesWitch(String userid){
        setKeyValue("VoicesWitch:"+userid,1,3);
    }

    public void setWithDrawRateLimit(String userid){
        setKeyValue("WithDrawRateLimit:"+userid,1,10);
    }

    public Integer getWithDrawRateLimit(String userid){
        return (Integer)getByKey("WithDrawRateLimit:"+userid);
    }


    //忘记密码
    public void setSmsKeyFgPsw(String mobile,String code){
        if (DataUtil.isEmpty(mobile) || DataUtil.isEmpty(code)){
            return;
        }
        setKeyValue("mobilePsw:"+mobile,code,600);
    }

    public String getSmsKeyFgPsw(String mobile){
        if(DataUtil.isEmpty(mobile)){
            return null;
        }
        Object object = super.getByKey("mobilePsw:" + mobile);
        if(DataUtil.isEmpty(object)){
            return null;
        }else{
            return object.toString();
        }
    }

    public void clearSmsKeyFgPsw(String mobile){
        if(DataUtil.isEmpty(mobile)){
            return;
        }
        super.deleteByKey("mobilePsw:" + mobile);
    }

    public void setPaySeller(Long orderid,Long seller){
        if (DataUtil.isEmpty(orderid) || DataUtil.isEmpty(seller)){
            return;
        }
        setKeyValue("payseller:" + orderid, seller,600);
    }

    public Long getPaySeller(Long orderid){
        if(DataUtil.isEmpty(orderid)){
            return null;
        }
        Object object = super.getByKey("payseller:" + orderid);
        if(DataUtil.isEmpty(object)){
            return null;
        }else{
            return Long.valueOf(object.toString());
        }
    }

    public void setRefundSmsKey(String mobile,String code){
        if (DataUtil.isEmpty(mobile) || DataUtil.isEmpty(code)){
            return;
        }
        setKeyValue("mobile-refund:"+mobile, code, 600);
    }

    public String getRefundSmsKey(String mobile){
        if(DataUtil.isEmpty(mobile)){
            return null;
        }
        Object object = super.getByKey("mobile-refund:" + mobile);
        if(DataUtil.isEmpty(object)){
            return null;
        }else{
            return object.toString();
        }
    }

    public void clearRefundSmsKey(String mobile){
        if(DataUtil.isEmpty(mobile)){
            return;
        }
        super.deleteByKey("mobile-refund:" + mobile);
    }

    public String getStaticCodeMoney(String payId,String channelType,String money){
        String todayMoney = null;
        String key = payId+"-"+channelType+"-"+DateUtil.getDate();
        Object object =getByKey(key);
        if (DataUtil.isNotEmpty(object)){
            todayMoney = (String) object;
            todayMoney = new BigDecimal(todayMoney).add(new BigDecimal(money)).toString();
        }else {
            todayMoney = money;
        }
        return todayMoney;
    }
    public void setStaticCodeMoney(String payId,String channelType,String money){
        String todayMoney = null;
        String key = payId+"-"+channelType+"-"+DateUtil.getDate();
        Object object =getByKey(key);
        if (DataUtil.isNotEmpty(object)){
            todayMoney = (String) object;
            todayMoney = new BigDecimal(todayMoney).add(new BigDecimal(money)).toString();
        }else {
            todayMoney = money;
        }
        setKeyValue(payId+"-"+channelType+"-"+DateUtil.getDate(),todayMoney, DateUtil.longTime());
    }


    public void addBannerPage(String pageText,String keys){
        String key = "banner-page"+keys;
        Object object =getByKey(key);
        if (DataUtil.isEmpty(object)){
            setKeyValue(key,pageText,600);
        }
    }
    public String getBannerPage(String keys){
        String key = "banner-page"+keys;
        return (String) getByKey(key);
    }
    public void deleteBannerPage(String keys){
        String key = "banner-page"+keys;
        deleteByKey(key);
    }

    public Integer getSaveAuthLimit(Long userid) {
        return (Integer)getByKey("SaveAuthLimit:"+userid);
    }

    public void setSaveAuthLimit(Long userid) {
        setKeyValue("SaveAuthLimit:"+userid,1,3);
    }

    public void setWechatApplyLimit(Long posid) {
        setKeyValue("WechatApplyLimit:" + posid, 1, 3);
    }

    public Integer getWechatApplyLimit(Long posid) {
        return (Integer)getByKey("WechatApplyLimit:" + posid);
    }

    public void setRequestValue(String key,String value,Integer time) {
        setKeyValue(key, value, time);
    }

    public String getRequestValue(String key) {
        Object object =getByKey(key);
        if (DataUtil.isNotEmpty(object)){
            return String.valueOf(object);
        }
        return null;
    }

    public void setValue(String key,String value) {
        setKeyValue(key, value, -1);
    }

    public String getValue(String key) {
        String value = String.valueOf(getByKey(key));
        deleteByKey(key);
        return value;
    }



    public void setValueByTime(String key,String value,int expireTime) {
        if(expireTime <= 0){
            expireTime = -1;
        }
        setKeyValue(key, value, expireTime);
    }

    public String getValueByTime(String key) {
        String value = String.valueOf(getByKey(key));
        return value;
    }

    public void setIPCurrentLimit(String key, String value, int expireTime) {
        if(expireTime <= 0){
            expireTime = -1;
        }
        setKeyValue(key, value, expireTime);
    }

    public String getIPCurrentLimit(String key) {
        return (String) getByKey(key);
    }

    public void setRebateClearingMonth(String id, String month, String value) {
        setKeyValue("rebateClearingMonth" + id + month, value, 600);
    }

    public String getRebateClearingMonth(String id, String month) {
        return (String) getByKey("rebateClearingMonth" + id + month);
    }

    public void deleteRebateClearingMonth(String id, String month) {
        deleteByKey("rebateClearingMonth" + id + month);
    }

    public void setEstimateClearingMonth(String id, String month, String value) {
        setKeyValue("estimateClearingMonth" + id + month, value, 600);
    }

    public String getEstimateClearingMonth(String id, String month) {
        return (String) getByKey("estimateClearingMonth" + id + month);
    }

    public void deleteEstimateClearingMonth(String id, String month) {
        deleteByKey("estimateClearingMonth" + id + month);
    }

    public void setYopPayToken(Long tradeId,String token,Integer timeout) {
        if (DataUtil.isEmpty(tradeId) || DataUtil.isEmpty(token)) {
            return;
        }
        setKeyValue("yoppay_trade_id:" + tradeId, token,timeout*60);
    }

    public String getYopToken (Long tradeId) {
        if (DataUtil.isEmpty(tradeId)) {
            return null;
        }
        return (String) getByKey("yoppay_trade_id:" + tradeId);
    }


    public void setPosName(String posName, Long id) {
        setKeyValue("pos-name-" + id, posName, -1);
    }

    public String getPosName(Long id) {
        Object object = getByKey("pos-name-" + id);
        if(DataUtil.isEmpty(object)){
            return null;
        } else {
            return (String) object;
        }
    }

    public void set(String key, String value, int expireTime) {
        if(expireTime <= 0){
            expireTime = -1;
        }
        setKeyValue(key, value, expireTime);
    }

    public String get(String key) {
        return (String) getByKey(key);
    }

    public void delete(String key){
        super.deleteByKey(key);
    }

}
*/
