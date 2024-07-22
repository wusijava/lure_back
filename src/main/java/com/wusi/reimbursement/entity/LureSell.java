package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class LureSell implements Identifiable<Long> {
    private Long id;

    private String productName;


    private String buyPrice;

    private String sellPrice;

    private Date buyDate;

    private Date sellDate;

    private String freight;

    private String profit;

    private Date createDate;

    private String buyImg;

    private String sellImg;

    private Integer state;

    private String uid;

    private String refund;

    private String remark;
}
