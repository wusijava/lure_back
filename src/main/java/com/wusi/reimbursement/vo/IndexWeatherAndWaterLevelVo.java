package com.wusi.reimbursement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IndexWeatherAndWaterLevelVo {

    /**
     * 白天天气描述
     */
    @ApiModelProperty(value = "白天天气描述")
    private String condTxtDay;
    /**
     * 夜晚天气描述
     */
    @ApiModelProperty(value = "夜晚天气描述")
    private String condTxtNight;

    /**
     * 日升时间
     */
    @ApiModelProperty(value = "日升时间")
    private String sunRise;
    /**
     * 日落时间
     */
    @ApiModelProperty(value = "日落时间")
    private String sunSet;
    /**
     * 最高温
     */
    @ApiModelProperty(value = "最高温")
    private String tmpMax;
    /**
     * 最低温
     */
    @ApiModelProperty(value = "最低温")
    private String tmpMin;

    /**
     * 风向
     */
    @ApiModelProperty(value = "风向")
    private String windDir;
    /**
     * 风力
     */
    @ApiModelProperty(value = "风力")
    private String windSc;

    /**
     * 气压
     */
    @ApiModelProperty(value = "气压")
    private Integer pres;
    //private String presDesc;
    /**
     * 水位
     */
    @ApiModelProperty(value = "水位")
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
