package com.wusi.reimbursement.wx.dto;

import lombok.Data;

@Data
public class Detail {
    private String strategy;
    private Integer errcode;
    private String suggest;
    private String label;
    private Integer prob;
}
