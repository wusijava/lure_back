package com.wusi.reimbursement.wx.dto;

import lombok.Data;

import java.util.List;

@Data
public class MsgApi {
    private String errcode;

    private String  errmsg;

    private Result result;

    private List<Detail> detail;

    private String trace_id;
}
