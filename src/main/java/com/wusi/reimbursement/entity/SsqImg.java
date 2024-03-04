package com.wusi.reimbursement.entity;

import com.wusi.reimbursement.common.Identifiable;
import lombok.Data;

import java.util.Date;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/29$ 10:58$
 */
@Data
public class SsqImg implements Identifiable<Long> {
    private Long id;
    /**
     * 期数
     */
    private String term;
    /**
     * 图片链接
     */
    private String url;
    /**
     * 时间
     */
    private Date createTime;
}
