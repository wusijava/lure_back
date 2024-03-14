package com.wusi.reimbursement.vo;

import lombok.Data;

import java.util.List;

@Data
public class ShuiWenWaterLevelVo {
    private long id;
    private List<ShuiWenWaterLevel> list;
    private String createTime;
}
