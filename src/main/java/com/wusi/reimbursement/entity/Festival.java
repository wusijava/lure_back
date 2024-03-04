package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class Festival implements Identifiable<Long> {

  private Long id;
  /**
   * 节日名称
   */
  private String name;
  /**
   * 毫秒数
   */
  private Long time;
  private Date createTime;



}
