package com.wusi.reimbursement.query;

import com.wusi.reimbursement.vo.SsqVo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SsqParam {
    private List<SsqVo> ssqList;
    private String term;
    private long totalPage;
    private String bonus;
    private String spend;
    private String commission;
    private String url;
}
