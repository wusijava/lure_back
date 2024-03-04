package com.wusi.reimbursement.vo;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

@Data
public class SpendList implements Identifiable<Long> {

  private Long id;
  private String item;
  private String price;
  private String consumer;
  private String date;
  private String remark;
  private String url;
  private String total;
  private String personTotal;



}
