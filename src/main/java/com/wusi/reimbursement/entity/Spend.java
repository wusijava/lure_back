package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class Spend  implements Identifiable<Long> {

  private Long id;
  private String item;
  private String price;
  private String consumer;
  private Date date;
  private String remark;
  private String url;




}
