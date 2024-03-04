package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class SystemLog implements Identifiable<Long>{

  private Long id;
  private String className;
  private String user;
  private String methodName;
  private String params;
  private Long exeuTime;
  private Date createTime;
  private String operation;
  private String ip;


}
