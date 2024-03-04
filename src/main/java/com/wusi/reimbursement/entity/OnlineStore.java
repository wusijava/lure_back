package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 在线门店
 */
@Data
public class OnlineStore implements Identifiable<Long> {
    private Long id;

    /**
     * 门店名称
     */
    private String storeName;

    /**
     * 门店编码
     */
    private String storeNo;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

}
