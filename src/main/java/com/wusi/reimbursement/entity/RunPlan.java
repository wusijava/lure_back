package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class RunPlan implements Identifiable<Long> {

  private Long id;
  /**
   * 姓名
   */
  private String name;
  /**
   * 时长
   */
  private String keepTime;
  /***
   * 距离
   */
  private String distance;
  /**
   * 时间
   */
  private Date createTime;
  /**
   * 图片地址
   */
  private String url;



}
