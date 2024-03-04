package com.wusi.reimbursement.vo;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

@Data
public class SellLogList implements Identifiable<Long> {

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
  private String url;


}
