package com.wusi.reimbursement.vo;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

@Data
public class SellLogListVo implements Identifiable<Long> {
  private Integer index;
  private Long id;
  private String product;
  private String buyerName;
  private String myOrderNo;
  private String sellMoney;
  private String amyOrderNo;
  private String buyMoney;
  private String profit;
  private String refund;
  private String remark;
  private String orderDate;
  public static String[] headers = {
          "序号",
          "商品名称",
          "买家姓名",
          "销售订单号",
          "销售金额",
          "购买订单号",
          "购买金额",
          "利润",
          "退款",
          "备注",
          "交易时间",

  };
/**
 * private Long id;
 *   private String product;
 *   private String buyerName;
 *   private String myOrderNo;
 *   private Integer sellMoney;
 *   private String amyOrderNo;
 *   private Integer buyMoney;
 *   private Integer profit;
 *   private Integer refund;
 *   private String remark;
 *   private String orderDate;**/
  public static String[] keys = {
          "index",
          "product",
          "buyerName",
          "myOrderNo",
          "sellMoney",
          "amyOrderNo",
          "buyMoney",
          "profit",
          "refund",
        "remark",
        "orderDate",
  };



}
