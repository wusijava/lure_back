package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 下架记录 wusi
 */
@Data
public class OffLineRecord implements Identifiable<Long> {

  private Long id;
  /**
   * 产品型号
   */
  private String model;
  /**
   * 产品链接
   */
  private String url;
  /**
   * 类型 1爱美  2自己
   */
  private String type;
  /**
   * 创建时间
   */
  private Date createTime;



}
