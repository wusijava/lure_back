package com.wusi.reimbursement.client;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wusi
 * @date 2020-8-23 14:23:58
 * @description 接口请求类
 */
public class ZcRequestClient {
    private static Logger log = LoggerFactory.getLogger(ZcRequestClient.class);

    private ApiRequestParam param;

    private Boolean checkSign;

    private String private_key;

    private String public_key;

    private String url;

    public ZcRequestClient(String app_id, String charset, String sign_type, String private_key, String public_key, String url, Boolean checkSign) {
        this.param = new ApiRequestParam();
        param.setApp_id(app_id);
        param.setCharset(charset);
        param.setSign_type(sign_type);
        this.private_key = private_key;
        this.public_key = public_key;
        this.url = url;
        this.checkSign = checkSign;
    }

    public ZcRequestClient() {
    }

    public <T extends ZcApiResult> T execute(ZcApiRequest<T> request) throws Exception {
        param.setContent(request.getContent());
        param.setMethod(request.getMethod());
        param.setNotify_url(request.getNotify_url());
        param.setRequest_id(request.getRequest_id());
        param.setVersion(request.getVersion());
        param.setTimestamp(request.getTimestamp());
        SignUtils.sign(param, private_key);
        T t = null;
        try {
            String result = HttpUtils.post(param, url);
            t = parser(result, request.responseClass());
        } catch (Exception e) {
            log.error("接口请求异常:", e);
            t = parser("{}", request.responseClass());
            t.setTimestamp(ApiUtils.getTimestamp());
            t.setRequest_id(request.getRequest_id());
            t.setVersion(request.getVersion());
            t.setMethod(request.getMethod());
            t.setApp_id(param.getApp_id());
            t.setCharset(param.getCharset());
            t.setSign_type(param.getSign_type());
            t.setCode(ApiCodeMsg.SYSTEM_ERROR.getCode());
            t.setMsg(ApiCodeMsg.SYSTEM_ERROR.getMsg());
            t.setError_msg(ApiErrorEnum.SYSTEM_ERROR.getMsg());
            t.setError_code(ApiErrorEnum.SYSTEM_ERROR.getCode());
            return t;
        }
        if (checkSign) {
            SignUtils.verifySign(t, public_key, false);
        }
        if (t.isSuccess()) {
            t.setModel(parser(t.getContent(), t.modelClass()));
        }
        return t;
    }

    <T> T parser(String content, Class<T> tClass) {
        return JSONObject.parseObject(content, tClass);
    }
}
