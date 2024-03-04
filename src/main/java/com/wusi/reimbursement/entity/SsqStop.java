package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class SsqStop implements Identifiable<Long> {

  private Long id;
  /**
   * 节日
   */
  private String festival;
  /**
   * 开始时间
   */
  private Date start;
  /**
   * 结束时间
   */
  private Date stop;
  /**
   * 创建时间
   */
  private Date createTime;

}
