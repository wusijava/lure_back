package com.wusi.reimbursement.vo;

import com.wusi.reimbursement.entity.Math;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ Description   :  封装每天作业
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/15$ 14:58$
 */
@Data
@AllArgsConstructor
public class MathParam {
    private List<Math> list;
    private String time;
    private Long totalPage;
    private String right;
    private String error;
    //总任务量
    private String task;
    //已做
    private String yiDo;
    //未做
    private String weiDo;
    private String rightToday;
    private String errorToday;
}
