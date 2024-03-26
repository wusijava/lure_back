package com.wusi.reimbursement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndexWeatherAndWaterLevelVoNow {
    @ApiModelProperty(value = "温度")
    private String temp;

    @ApiModelProperty(value = "图标编码")
    private String icon;

    @ApiModelProperty(value = "天气描述")
    private String text;

    @ApiModelProperty(value = "风向")
    private String windDir;

    @ApiModelProperty(value = "风力登记")
    private String windScale;

    @ApiModelProperty(value = "风速")
    private String windSpeed;


    @ApiModelProperty(value = "湿度")
    private String humidity;

    @ApiModelProperty(value = "气压")
    private String pressure;

    @ApiModelProperty(value = "长江水文水位")
    private String waterLevel;

    /**
     * 涨1 退-1 持平0
     */
    private Integer downOrUp;

    /**
     * 退或涨的值
     */
    private String  value;


}
