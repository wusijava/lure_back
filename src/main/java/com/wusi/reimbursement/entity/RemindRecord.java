package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * @ Description   :  提醒记录
 * @ Author        :  wusi
 * @ CreateDate    :  2020/12/30$ 12:55$
 */
@Data
public class RemindRecord  implements Identifiable<Long> {
    private Long id;
    private Date createTime;
    private String timeStr;
}
