package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class LureShopping implements Identifiable<Long> {

  private Long id;
  private String item;
  private String price;
  private Date date;
  private String remark;
  private String url;
  private String uid;
  private String userName;
  private Integer recommend;
}