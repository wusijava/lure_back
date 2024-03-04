package com.wusi.reimbursement.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GuiWang {
    /**
     * 姓名
     */
    private String name;

    /**
     * 打龟率
     */
    private BigDecimal rate;
    private String rateStr;

    /**
     * 名次
     */
    private String index;

}
