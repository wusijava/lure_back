package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 天气
 */
@Data
public class Weather implements Identifiable<Long> {

  private Long id;
  /**
   * 城市编码
   */
  private String cid;
  /**
   * 区
   */
  private String location;
  /**
   * 市
   */
  private String parentCity;
  /**
   * 省
   */
  private String province;
  /**
   * 国
   */
  private String country;
  /**
   * 纬度
   */
  private String lat;
  /**
   * 经度
   */
  private String lon;
  /**
   * 时区
   */
  private String timeZone;
  /**
   * 数据更新当地时间
   */
  private String loc;
  /**
   * 数据更新的UTC时间
   */
  private String utc;
  /**
   * 白天天气描述
   */
  private String condTxtDay;
  /**
   * 夜晚天气描述
   */
  private String condTxtNight;
  /**
   * 日期
   */
  private Date date;
  /**
   * 湿度
   */
  private String hum;
  /**
   * 月升时间
   */
  private String moonRise;
  /**
   * 月落时间
   */
  private String moonSet;
  /**
   * 气压
   */
  private Integer pres;
  /**
   * 日升时间
   */
  private String sunRise;
  /**
   * 日落时间
   */
  private String sunSet;
  /**
   * 最高温
   */
  private String tmpMax;
  /**
   * 最低温
   */
  private String tmpMin;
  /**
   * 风向
   */
  private String windDir;
  /**
   * 风力
   */
  private String windSc;
  /***
   * 风速
   */
  private String windSpd;
  /**
   * 时间
   */
  private Date createTime;
}
