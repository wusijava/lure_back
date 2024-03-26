package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

@Data
public class ShuiWenWaterLevel implements Identifiable<Long> {

    private Long id;

    private String data;

    private Date createTime;

    /**
     * -1退 1涨 0持平
     */
    private Integer downOrUp;

    private String value;
}
