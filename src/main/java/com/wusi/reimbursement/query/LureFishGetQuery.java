package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.LureFishGet;
import lombok.Data;

/**
 * @ Description   :  天气查询
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class LureFishGetQuery extends LureFishGet {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;

    private String notUid;

    private String time;

    private String type;
}
