package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class SellLog implements Identifiable<Long> {

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
  private Date orderDate;
  private String url;



}
