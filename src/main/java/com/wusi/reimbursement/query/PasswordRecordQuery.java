package com.wusi.reimbursement.query;

import com.wusi.reimbursement.entity.PasswordRecord;
import lombok.Data;

/**
 * @ Description   :  密码query
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/9$ 11:10$
 */
@Data
public class PasswordRecordQuery extends PasswordRecord {
    private Integer page;

    private Integer limit;

    private Integer offset;
    private String startTime;

    private String endTime;
}
