package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class WaterLevel implements Identifiable<Long> {

  private Long id;
  /**
   * 水位 米
   */
  private String waterLevel;
  /**
   * 地址
   */
  private String address;
  /**
   * 链接
   */
  private String href;
  private Date createTime;



}
