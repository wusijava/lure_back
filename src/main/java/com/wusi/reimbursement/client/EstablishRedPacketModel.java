package com.wusi.reimbursement.client;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: huze
 * @Date: 2020/12/10 15:34
 */
@Data
public class EstablishRedPacketModel extends ZcApiModel {

    private static final long serialVersionUID = -330917308635622354L;

    /**
     * 第三方单号
     */
    @JSONField(name = "out_trade_no")
    @NotEmpty(message = "缺少第三方单号")
    private String outTradeNo;

    /**
     * 红包金额
     */
    @JSONField(name = "amount")
    @NotEmpty(message = "缺少金额")
    private String amount;

    /**
     * 借款订单号
     */
    @JSONField(name = "brw_ord_no")
    @NotEmpty(message = "缺少借款订单号")
    private String brwOrdNo;
    /**
     * 红包标题
     */
    @JSONField(name = "title")
    @NotEmpty(message = "缺少红包标题")
    private String title;
    /**
     * 类型 1支付宝 2乐薪 3 网商
     */
    @JSONField(name = "type")
    @NotEmpty(message = "缺少红包类型")
    private Integer type;

    /**
     * 商户号
     */
    @JSONField(name = "merchant_no")
    @NotEmpty(message = "缺少商户号")
    private String merchantNo;

    /**
     * 收银员手机号
     */
    @JSONField(name = "cashier_phone_no")
    @NotEmpty(message = "缺少收银员手机号")
    private String cashierPhoneNo;

    /**
     * 收款账号
     */
    @JSONField(name = "seller_no")
    @NotEmpty(message = "缺少收款账号")
    private String sellerNo;

    /**
     * 顾客手机号
     */
    @JSONField(name = "custom_phone")
    @NotEmpty(message = "缺少顾客手机号")
    private String customPhone;

    /**
     * 延期到账天数
     */
    @JSONField(name = "delay_days")
    @NotEmpty(message = "缺少延期到账天数")
    private Integer delayDays;

    /**
     * 是否隔月到账 1是  0否
     */
    @JSONField(name = "is_next_month_settle")
    @NotEmpty(message = "缺少是否隔月到账")
    private Integer isNextMonthSettle;

    /**
     * 红包领取账号
     */
    @JSONField(name = "collect_account_number")
    @NotEmpty(message = "缺少红包领取账号")
    private String collectAccountNumber;

    /**
     * 省份
     */
    @JSONField(name = "province_name")
    @NotEmpty(message = "缺少省份")
    private String provinceName;
    /**
     * 下单营业员编号
     */
    @JSONField(name = "cashier_no")
    @NotEmpty(message = "缺少下单营业员编号")
    private String cashierNo;

    /**
     * 省份
     */
    @JSONField(name = "province_code")
    @NotEmpty(message = "缺少省份")
    private String provinceCode;

}
