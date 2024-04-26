package com.wusi.reimbursement.wx.dto;


public class BaseApiRes {

    private Integer errcode;

    private String errmsg;

    public boolean isSuccess() {
        if (errcode == null){
            return false;
        }
        return 0 == errcode;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
