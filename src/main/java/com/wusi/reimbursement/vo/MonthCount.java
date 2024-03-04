package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.List;


@Data
public class MonthCount {
    private String month;
    private Integer num;
    private Boolean show=false;
    private List<FishCount> fishCount;
}
