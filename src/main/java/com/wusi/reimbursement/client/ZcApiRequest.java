package com.wusi.reimbursement.client;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author wusi
 * @description 返回结果
 * @date 2019-10-30 09:42:47
 */
public abstract class ZcApiRequest<T> implements Serializable {
    private static final long serialVersionUID = 8870290266883951058L;

    protected String method;

    protected String request_id;

    protected String version;

    protected String timestamp;

    private String notify_url;

    private String content;

    private ZcApiModel model;

    public String getMethod() {
        return method;
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public ZcApiRequest() {
        this.request_id = ApiUtils.getRequestId();
        this.timestamp = ApiUtils.getTimestamp();
    }

    public ZcApiModel getModel() {
        return model;
    }

    public void setModel(ZcApiModel model) {
        this.model = model;
        this.content = model == null ? null : JSONObject.toJSONString(model);
    }

    public abstract Class<T> responseClass();
}
