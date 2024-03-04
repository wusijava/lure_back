package com.wusi.reimbursement.client;


/**
 * @author wusi
 * @description 对外接口枚举
 * 方法命名规则  统一前缀.模块名称.模块内操作对象泛称.执行操作
 * 统一前缀 com.zanclick.zcpay
 * @date 2020-8-22 11:29:45
 */
public enum ApiMethod {
    ESTABLISH_REDPACKET("com.zanclick.redpacket.establish.redpacket","红包创建接口","1.0");

    public String method;

    public String desc;

    public String version;

    ApiMethod(String method, String desc, String version) {
        this.method = method;
        this.desc = desc;
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
