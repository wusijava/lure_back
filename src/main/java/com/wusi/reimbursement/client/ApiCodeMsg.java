package com.wusi.reimbursement.client;

/**
 * 网关返回码
 */
public enum ApiCodeMsg {
    FAIL("40500", "操作失败"),
    SUCCESS("20000", "操作成功"),
    SYSTEM_ERROR("10500", "系统异常");

    ApiCodeMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
