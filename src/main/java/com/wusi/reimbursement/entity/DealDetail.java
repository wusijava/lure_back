package com.wusi.reimbursement.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class DealDetail {
    private Integer fq_num;
    /**手续费承担方*/
    private Integer seller_type;
    /**交易金额*/
    private String deal_money;
    /**交易手续费率*/
    private String deal_rate;
    /**交易手续费*/
    private String deal_fee;
    /**分期费率*/
    private String fq_rate;
    private String fq_fee;
    /**成本费率*/
    private String cost_rate;
    private String cost_fee;
    /**收益*/
    private String fq_profit_money;
    /**顾客实付*/
    private String customer_real_money;
    /**该笔订单实际金额*/
    private String order_real_money;
    /**商户实收*/
    private String business_real_money;
    /**每期应还*/
    private String fq_each_money;

    private String first_each_money;

    public DealDetail() {
        this.fq_num = 0;
        this.seller_type = 0;
        this.deal_money = "0.00";
        this.deal_rate = "0.00";
        this.deal_fee = "0.00";
        this.fq_rate = "0.00";
        this.fq_fee = "0.00";
        this.cost_rate = "0.00";
        this.cost_fee = "0.00";
        this.fq_profit_money = "0.00";
        this.customer_real_money = "0.00";
        this.order_real_money = "0.00";
        this.business_real_money = "0.00";
        this.fq_each_money = "0.00";
        this.first_each_money = "0.00";
    }


    public void setFq_num(Integer fq_num) {
        if (fq_num == null){
            return;
        }
        this.fq_num = fq_num;
    }

    public void setSeller_type(Integer seller_type) {
        if (seller_type == null){
            return;
        }
        this.seller_type = seller_type;
    }

    public void setDeal_money(String deal_money) {
        if (deal_money == null){
            return;
        }
        this.deal_money = deal_money;
    }


    public void setDeal_rate(String deal_rate) {
        if (deal_rate == null){
            return;
        }
        this.deal_rate = deal_rate;
    }
    public void setDeal_fee(String deal_fee) {
        if (deal_fee == null){
            return;
        }
        this.deal_fee = deal_fee;
    }

    public void setFq_rate(String fq_rate) {
        if (fq_rate == null){
            return;
        }
        this.fq_rate = fq_rate;
    }


    public void setFq_fee(String fq_fee) {
        if (fq_fee == null){
            return;
        }
        this.fq_fee = fq_fee;
    }


    public void setCost_rate(String cost_rate) {
        if (cost_rate == null){
            return;
        }
        this.cost_rate = cost_rate;
    }

    public void setCost_fee(String cost_fee) {
        if (cost_fee == null){
            return;
        }
        this.cost_fee = cost_fee;
    }

    public void setFq_profit_money(String fq_profit_money) {
        if (fq_profit_money == null){
            return;
        }
        this.fq_profit_money = fq_profit_money;
    }

    public void setCustomer_real_money(String customer_real_money) {
        if (customer_real_money == null){
            return;
        }
        this.customer_real_money = customer_real_money;
    }

    public void setOrder_real_money(String order_real_money) {
        if (order_real_money == null){
            return;
        }
        this.order_real_money = order_real_money;
    }


    public void setBusiness_real_money(String business_real_money) {
        if (business_real_money == null){
            return;
        }
        this.business_real_money = business_real_money;
    }

    public void setFq_each_money(String fq_each_money) {
        if (fq_each_money == null){
            return;
        }
        this.fq_each_money = fq_each_money;
    }


    public void setFirst_each_money(String first_each_money) {
        if (first_each_money == null){
            return;
        }
        this.first_each_money = first_each_money;
    }


    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
