package com.wusi.reimbursement.wx.dto;




/**
 * @Description 描述
 * @Author duchong
 * @Date 2023/1/3 11:38
 * @Version 1.0.0
 **/
public class AccessToken {

  private String access_token;

  private String expires_in;

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getExpires_in() {
    return expires_in;
  }

  public void setExpires_in(String expires_in) {
    this.expires_in = expires_in;
  }
}
