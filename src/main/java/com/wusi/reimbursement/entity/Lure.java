package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class Lure  implements Identifiable<Long> {

  private Long id;
  private String lure;
  private Date createTime;


}
