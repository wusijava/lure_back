package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class LurePositionRecord  implements Identifiable<Long> {

  private Long id;
  private String name;
  private String province;
  private String city;
  private String district;
  private String lng;
  private String lat;
  private String address;
  private String image1;
  private String image2;
  private String image3;
  private String image4;
  private String image5;
  private String image6;
  private String video;
  private Date createTime;
  private String uid;
  private String userName;
}
