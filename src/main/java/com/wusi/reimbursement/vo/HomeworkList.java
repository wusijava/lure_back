package com.wusi.reimbursement.vo;


import lombok.Data;


@Data
public class HomeworkList  {

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
  private String createTime;
  /**
   * 评价
   */
  private String evaluate;
  /**
   * 备注
   */
  private String remark;



}
