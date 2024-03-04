package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 线上信用购产品
 */
@Data
public class OnlineProduct implements Identifiable<Long> {
    private Long id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 价格
     */
    private String price;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;
}
