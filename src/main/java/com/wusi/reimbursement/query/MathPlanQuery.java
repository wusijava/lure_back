package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.MathPlan;
import lombok.Data;

/**
 * @ Description   :  作业计划
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class MathPlanQuery extends MathPlan {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
