package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.List;

@Data
public class ShuiWenWaterLevelVo {
    private long id;
    private List<ShuiWenWaterLevelJson> list;
    private String createTime;
}
