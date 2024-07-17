package com.wusi.reimbursement.wx.dto;

import lombok.Data;

@Data
public class ImgResult {
    private String trace_id;

    private Result result;

    private Detail detail;
}
