package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;
@Data
public class LureFishKind implements Identifiable<Long> {

  private Long id;
  private String name;
  private Date createTime;
  private Integer state;

}
