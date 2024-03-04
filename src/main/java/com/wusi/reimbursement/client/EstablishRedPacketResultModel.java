package com.wusi.reimbursement.client;

import lombok.Data;

/**
 * @Author: huze
 * @Date: 2020/12/10 15:35
 */
@Data
public class EstablishRedPacketResultModel extends ZcApiModel{
    private static final long serialVersionUID = 3269889033515667555L;
    /**
     * 红包编号
     */
    private String packetNo;

    /***
     * 第三方订单号
     */
    private String outTradeNo;

    /**
     * 创建说明（失败，成功）
     */
    private String reason;
}
