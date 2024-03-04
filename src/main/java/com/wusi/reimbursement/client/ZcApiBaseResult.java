package com.wusi.reimbursement.client;

import java.util.Objects;

/**
 * @author wusi
 * @description 错误返回码
 * @date 2019-11-23 14:14:07
 */
public class ZcApiBaseResult {

    private String error_code;

    private String error_msg;

    private String code;

    private String msg;

    public ZcApiBaseResult() {
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZcApiBaseResult that = (ZcApiBaseResult) o;
        return Objects.equals(error_code, that.error_code) &&
                Objects.equals(error_msg, that.error_msg) &&
                Objects.equals(code, that.code) &&
                Objects.equals(msg, that.msg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error_code, error_msg, code, msg);
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "error_code='" + error_code + '\'' +
                ", error_msg='" + error_msg + '\'' +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
