package com.wusi.reimbursement.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * 金额计算详情
 *
 * @author wusi
 * @date 2019-7-16 11:59:37
 */
@Data
public class CalculateDetail {
    /**
     * 交易期数
     */
    private Integer fqNum;
    /**
     * 交易金额
     */
    private String money;

    private String totalMoney;
    /**
     * 利息
     */
    private String interest;
    /**
     * 交易金额
     */
    private String rate;

    /**
     * 每期应还
     */
    private String each;

    /**
     * 特殊应还
     */
    private String special;

    public CalculateDetail() {
        this.fqNum = 0;
        this.money = "0.00";
        this.rate = "0.00";
        this.interest = "0.00";
        this.each = "0.00";
        this.special = "0.00";
    }


    public void setFqNum(Integer fqNum) {
        if (fqNum == null) {
            return;
        }
        this.fqNum = fqNum;
    }

    public void setMoney(String money) {
        if (money == null) {
            return;
        }
        this.money = money;
    }

    public void setInterest(String interest) {
        if (interest == null) {
            return;
        }
        this.interest = interest;
    }

    public void setRate(String rate) {
        if (rate == null) {
            return;
        }
        this.rate = rate;
    }


    public void setEach(String each) {
        if (each == null) {
            return;
        }
        this.each = each;
    }

    public void setSpecial(String special) {
        if (special == null) {
            return;
        }
        this.special = special;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
