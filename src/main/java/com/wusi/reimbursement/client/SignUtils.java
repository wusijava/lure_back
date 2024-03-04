package com.wusi.reimbursement.client;


import com.wusi.reimbursement.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author wusi
 * @description 签名类
 * @date 2019-11-23 14:21:19
 */
public class SignUtils {
    private static Logger log = LoggerFactory.getLogger(SignUtils.class);

    /**
     * 请求值验签
     *
     * @param request   请求参数
     * @param publicKey 眼前公钥
     */
    public static Boolean verifySign(ApiRequestParam request, String publicKey) {
        Boolean flag = false;
        try {
            Map<String, String> param = ObjectUtils.toStringMap(request);
            flag = RSAUtils.rsaCheck(param, publicKey);
        } catch (Exception e) {
            log.error("验签失败:", e);
            flag = false;
        }
        return flag;
    }

    /**
     * 返回值验签
     *
     * @param result     返回参数
     * @param publicKey  验签公钥
     * @param forceCheck 是否强制校验签名
     */
    public static Boolean verifySign(ZcApiResult result, String publicKey, Boolean forceCheck) throws Exception {
        Map<String, String> param = ObjectUtils.toStringMap(result);
        return RSAUtils.rsaCheck(param, publicKey, forceCheck);
    }

    /**
     * 返回值签名
     *
     * @param response   返回参数签名
     * @param privateKey 加签私钥
     */
    public static void sign(ZcApiResult response, String privateKey) {
        if (ObjectUtils.isEmpty(privateKey)) {
            return;
        }
        try {
            Map<String, String> param = ObjectUtils.toStringMap(response);
            String sign = RSAUtils.rsaSign(param, privateKey);
            response.setSign(sign);
        } catch (Exception e) {
            log.error("签名信息异常:", e);
            throw new BaseException("签名异常");
        }
    }

    /**
     * 请求值签名
     *
     * @param request    请求参数
     * @param privateKey 签名私钥
     */
    public static void sign(ApiRequestParam request, String privateKey) throws Exception {
        Map<String, String> param = ObjectUtils.toStringMap(request);
        String sign = RSAUtils.rsaSign(param, privateKey);
        request.setSign(sign);
    }


}
