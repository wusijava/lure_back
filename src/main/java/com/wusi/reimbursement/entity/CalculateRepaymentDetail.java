package com.wusi.reimbursement.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 还款金额计算详情
 *
 * @author wusi
 * @date 2019-7-16 11:59:37
 */
@Data
public class CalculateRepaymentDetail {

    /**
     * 冻结金额
     */
    private String payAmt;

    /**
     * 结算金额
     */
    private String loanAmt;

    /**
     * 交易期数
     */
    private Integer fqNum;

    /**
     * 费率
     */
    private String rate;

    /**
     * 利息
     */
    private String interest;

    /**
     * 每期本金
     */
    private String each;

    /**
     * 每期利息
     */
    private String eachInterest;

    /**
     * 每期本息
     */
    private String eachTotal;

    /**
     * 特殊本金（首期/尾期）
     */
    private String special;

    /**
     * 特殊利息（首期/尾期）
     */
    private String specialInterest;

    /**
     * 特殊本息（首期/尾期）
     */
    private String specialTotal;

    public CalculateRepaymentDetail() {
        this.fqNum = 0;
        this.payAmt = "0.00";
        this.loanAmt = "0.00";
        this.rate = "0.00";
        this.interest = "0.00";
        this.each = "0.00";
        this.eachInterest = "0.00";
        this.eachTotal = "0.00";
        this.special = "0.00";
        this.specialInterest = "0.00";
        this.specialTotal = "0.00";
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
