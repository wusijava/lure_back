package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.AwardRecord;
import lombok.Data;

/**
 * @ Description   :  奖励query
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class AwardRecordQuery extends AwardRecord {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
