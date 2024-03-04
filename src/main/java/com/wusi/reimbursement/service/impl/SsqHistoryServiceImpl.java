package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SsqHistory;
import com.wusi.reimbursement.mapper.SsqHistoryMapper;
import com.wusi.reimbursement.service.SsqHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * @date 2021-01-12 16:22:33
 **/
@Service
public class SsqHistoryServiceImpl extends BaseMybatisServiceImpl<SsqHistory,Long> implements SsqHistoryService {

    @Autowired
    private SsqHistoryMapper ssqHistoryMapper;


    @Override
    protected BaseMapper<SsqHistory, Long> getBaseMapper() {
        return ssqHistoryMapper;
    }

    @Override
    public List<Map<String, Object>> getValue(Integer count) {
        return ssqHistoryMapper.getValue(count);
    }
}
