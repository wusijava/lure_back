package com.wusi.reimbursement.client;



import org.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * @author wusi
 * @description 接口请求参数校验
 * @date 2019-11-23 14:14:14
 */
public class ApiRequestParam {

    private String app_id;

    private String charset;

    private String method;

    private String sign_type;

    private String timestamp;

    private String request_id;

    private String version;

    private String notify_url;

    private String sign;

    private String content;

    public String check() {
        if (ObjectUtils.isEmpty(app_id)) {
            return "app_id 不能为空";
        }
        if (ObjectUtils.isEmpty(charset)) {
            return "缺少charset 不能为空";
        }
        if (ObjectUtils.isEmpty(method)) {
            return "method 不能为空";
        }
        if (!charset.equalsIgnoreCase("UTF-8")) {
            return "charset 格式不正确";
        }
        if (ObjectUtils.isEmpty(sign_type)) {
            return "sign_type 不能为空";
        }
        if (!sign_type.equalsIgnoreCase("RSA2")) {
            return "sign_type 格式不正确";
        }
        if (ObjectUtils.isEmpty(timestamp)) {
            return "timestamp 不能为空";
        }
        if (ObjectUtils.isEmpty(request_id)) {
            return "request_id 不能为空";
        }
        if (ObjectUtils.isEmpty(version)) {
            return "version 不能为空";
        }
        if (ObjectUtils.isEmpty(sign)) {
            return "sign 不能为空";
        }
        return null;
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

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
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

    public ApiRequestParam() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiRequestParam that = (ApiRequestParam) o;
        return Objects.equals(app_id, that.app_id) &&
                Objects.equals(charset, that.charset) &&
                Objects.equals(method, that.method) &&
                Objects.equals(sign_type, that.sign_type) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(request_id, that.request_id) &&
                Objects.equals(version, that.version) &&
                Objects.equals(notify_url, that.notify_url) &&
                Objects.equals(sign, that.sign) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app_id, charset, method, sign_type, timestamp, request_id, version, notify_url, sign, content);
    }

    @Override
    public String toString() {
        return "ApiRequestParam{" +
                "app_id='" + app_id + '\'' +
                ", charset='" + charset + '\'' +
                ", method='" + method + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", request_id='" + request_id + '\'' +
                ", version='" + version + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", sign='" + sign + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
