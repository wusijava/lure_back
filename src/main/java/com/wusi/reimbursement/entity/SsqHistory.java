package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class SsqHistory implements Identifiable<Long> {

  private Long id;
  private String term;
  private String red1;
  private String red2;
  private String red3;
  private String red4;
  private String red5;
  private String red6;
  private String blue;
  private Date createTime;
  private String bonusTime;

}
