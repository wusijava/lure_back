package com.wusi.reimbursement.vo;

import lombok.Data;

@Data
public class PaiHang {
    private String index;
    private String name;
    private Integer num;
    private String desc;
    /**
     * 头像地址
     */
    private String img;
}
