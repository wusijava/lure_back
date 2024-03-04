package com.wusi.reimbursement.client;

/**
 * @author wusi
 * @description 异常枚举
 * @date 2020-8-22 11:29:45
 */
public enum ApiErrorEnum implements EnumInterface {

    /**
     * api异常枚举
     */
    SYSTEM_ERROR("SYSTEM_ERROR", "系统异常"),
    INVALID_IP_ADDRESS("INVALID_IP_ADDRESS", "ip地址不在白名单之内"),
    API_NOT_EXIST("API_NOT_EXIST", "API方法不存在"),
    MISSING_PUBLIC_PARAM("MISSING_PUBLIC_PARAM", "缺少公共参数 >> {0}"),
    INVALID_PUBLIC_PARAM("INVALID_PUBLIC_PARAM", "无效公共参数 >> {0}"),
    INVALID_PARAM("INVALID_PARAM", "无效参数 >> 参数[{0}] >> 原因[{1}]"),
    INVALID_SIGN("INVALID_SIGN", "无效签名"),
    BUSINESS_ERROR("BUSINESS_ERROR", "业务异常 >> 原因[{0}]"),
    ;

    ApiErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
