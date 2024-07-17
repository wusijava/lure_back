package com.wusi.reimbursement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CreateCollectivity {
    @ApiModelProperty(value = "路亚开始时间")
    private  String lureDate;

    @ApiModelProperty(value = "标点名称")
    private  String address;

    @ApiModelProperty(value = "标点经度")
    private  String lng;

    @ApiModelProperty(value = "标点纬度")
    private  String lat;

    @ApiModelProperty(value = "备注")
    private  String remark;

    @ApiModelProperty(value = "活动名称")
    private  String activityName;

    @ApiModelProperty(value = "活动口号")
    private  String slogan;

    @ApiModelProperty(value = "微信code")
    private String wxCode;



}
