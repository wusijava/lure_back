package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class Address implements Identifiable<Long> {

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
   * 经度
   */
  private String lng;
  /**
   * 纬度
   */
  private String lat;
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
  private Date createTime;



}
