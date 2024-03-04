package com.wusi.reimbursement.vo;

import lombok.Data;


@Data
public class AddressVO {

  private Long id;
  /**
   * 省
   */
  private String province;
  /**
   * 市
   */
  private String city;
  /**
   * 区
   */
  private String district;
  /**
   * 经纬度
   */
  private String jwd;
  /**
   * 详细地址
   */
  private String address;
  /**
   * 名字
   */
  private String name;
  /**
   * 时间
   */
  private String createTime;



}
