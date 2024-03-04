package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.WaterLevel;
import lombok.Data;

/**
 * @ Description   :  水位查询
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class WaterLevelQuery extends WaterLevel {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
