package com.wusi.reimbursement.client;


import java.util.Objects;

/**
 * @author wusi
 * @description
 * @date 2020-5-28 11:56:42
 */
public abstract class ZcApiResult<T> extends ZcApiBaseResult {

    private String app_id;

    private String charset;

    private String method;

    private String sign_type;

    private String timestamp;

    private String request_id;

    private String version;

    private String sign;

    private String content;

    private T model;

    public ZcApiResult() {
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public boolean isSuccess(){
        return ApiCodeMsg.SUCCESS.getCode().equals(super.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZcApiResult apiResult = (ZcApiResult) o;
        return Objects.equals(app_id, apiResult.app_id) &&
                Objects.equals(charset, apiResult.charset) &&
                Objects.equals(method, apiResult.method) &&
                Objects.equals(sign_type, apiResult.sign_type) &&
                Objects.equals(timestamp, apiResult.timestamp) &&
                Objects.equals(request_id, apiResult.request_id) &&
                Objects.equals(version, apiResult.version) &&
                Objects.equals(sign, apiResult.sign) &&
                Objects.equals(content, apiResult.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app_id, charset, method, sign_type, timestamp, request_id, version, sign, content);
    }


    public abstract Class<T> modelClass();


    @Override
    public String toString() {
        return "ApiResult{" +
                "app_id='" + app_id + '\'' +
                ", charset='" + charset + '\'' +
                ", method='" + method + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", request_id='" + request_id + '\'' +
                ", version='" + version + '\'' +
                ", sign='" + sign + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
