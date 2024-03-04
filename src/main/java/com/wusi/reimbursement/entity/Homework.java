package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 记录小柠檬作业实体类
 */

@Data
public class Homework implements Identifiable<Long> {

  private Long id;
  /**
   * 姓名
   */
  private String name;
  /**
   * 学科
   */
  private String subject;
  /**
   * 内容
   */
  private String content;
  /**
   * 耗时
   */
  private String timeConsuming;
  /**
   * 图片地址
   */
  private String url;
  /**
   * 时间
   */
  private Date createTime;
  /**
   * 评价
   */
  private String evaluate;
  /**
   * 备注
   */
  private String remark;



}
