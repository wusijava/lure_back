package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class SsqQuick implements Identifiable<Long> {

  private Long id;
  private String user;
  private String red1;
  private String red2;
  private String red3;
  private String red4;
  private String red5;
  private String red6;
  private String blue;
  private Date createTime;
  /**
   * 状态 1启用  0关闭
   */
  private Integer state;



}
