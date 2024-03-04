package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.ClassPlan;
import lombok.Data;

import java.util.List;

/**
 * @ Description   :  排班Query
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class ClassPlanQuery extends ClassPlan {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;
    private List<String > dateList;
    private String endTime;
    private String dateRange;
}
