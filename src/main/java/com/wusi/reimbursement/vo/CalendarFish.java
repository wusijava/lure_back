package com.wusi.reimbursement.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CalendarFish {
    private String date;

    private Integer getFish;

    @ApiModelProperty(value = "鱼获描述")
    private String fishDesc;

}
