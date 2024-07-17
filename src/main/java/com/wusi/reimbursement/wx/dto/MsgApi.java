package com.wusi.reimbursement.wx.dto;

import lombok.Data;

@Data
public class MsgApi {
    private String errcode;

    private String  errmsg;

    private Result result;

    //private Detail detail;

    private String trace_id;
}
