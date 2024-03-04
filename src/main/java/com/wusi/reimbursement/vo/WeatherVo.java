package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * 天气
 */
@Data
public class WeatherVo implements Identifiable<Long> {

  private Long id;
  /**
   * 区
   */
  private String location;
  /**
   * 数据更新当地时间
   */
  private String loc;
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
  private String date;
  /**
   * 湿度
   */
  private String hum;
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
   * 温度
   */
  private String temp;
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
}
