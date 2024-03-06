package com.wusi.reimbursement.vo;

import lombok.Data;

@Data
public class SaveFish {
    private String fishKind;
    private String weight;
    private String length;
    private String lure;
    private String url;
    private String lng;
    private String lat;
    private String province;
    private String city ;
    private String district;
    private String address;
    private String orderDate;
    private String remark;
    private Integer getFish;
    private Integer eatFish;
    private Integer num;
    private String date;
}
