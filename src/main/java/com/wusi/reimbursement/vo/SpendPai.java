package com.wusi.reimbursement.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpendPai {
    /**
     * 姓名
     */
    private String name;

    /**
     * 总花费
     */
    private BigDecimal spend;

    /**
     * 鱼获数
     */
    private Long num;

    /**
     * 鱼单价
     */
    private String price;

    /**
     * 排名
     */
    private String index;

}
