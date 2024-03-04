package com.wusi.reimbursement.vo;

import lombok.Data;


/**
 * 吴思 2020-12-14 淘宝产品VO
 */
@Data
public class ProductNewVO {

  private Long id;
  /**
   * 产品型号
   */
  private String model;

  /**
   * 爱美状态
   */
  private String amyState;
  private String amyStateDesc;
  /**
   * 我的状态
   */
  private String myState;
  private String myStateDesc;
  /**
   * 产品图片url
   */
  private String image;


}
