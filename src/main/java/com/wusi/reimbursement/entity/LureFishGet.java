package com.wusi.reimbursement.entity;


import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class LureFishGet implements Identifiable<Long> {

  private Long id;
  private String fishKind;
  private String weight;
  private String length;
  private Integer use;
  private String lure;
  private String province;
  private String city;
  private String district;
  private String lng;
  private String lat;
  private String address;
  private String imageUrl;
  private String videoUrl;
  private String condTxtDay;
  private String condTxtNight;
  private String hum;
  private String pres;
  private String sunRise;
  private String sunSet;
  private String tmpMax;
  private String tmpMin;
  private String windDir;
  private String windDc;
  private String windSpd;
  private Date createTime;
  private String remark;
  /**
   * -1打龟  1收货
   */
  private Integer getFish;

  /**
   * uid
   */
  private String uid;

  private String userName;

  /**
   * 是否重复 0否 1是
   */
  private Integer isRepeat;


  /**
   * 图片验证id
   */
  private String traceId;

  /**
   * 0待审核 1通过 -1驳回
   */
  private Integer state;

  public enum IsRepeat {
    no(0, "不重复"),
    yes(1, "重复的");

    private Integer code;
    private String desc;

    IsRepeat(Integer code, String desc) {
      this.code = code;
      this.desc = desc;
    }

    public Integer getCode() {
      return code;
    }

    public void setCode(Integer code) {
      this.code = code;
    }

    public String getDesc() {
      return desc;
    }

    public void setDesc(String desc) {
      this.desc = desc;
    }
  }

}
