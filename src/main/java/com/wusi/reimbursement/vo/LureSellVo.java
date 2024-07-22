package com.wusi.reimbursement.vo;

import lombok.Data;

@Data
public class LureSellVo {
    private Long id;

    private String productName;


    private String buyPrice;

    private String sellPrice;

    private String buyDate;

    private String sellDate;

    private String freight;

    private String profit;


    private String buyImg;

    private String sellImg;

    private Integer state;

    private String stateDesc;


    private String refund;

    private String remark;

    private Long storeDay;

}
