package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.ShuiWenWaterLevel;
import com.wusi.reimbursement.mapper.ShuiWenWaterLevelMapper;
import com.wusi.reimbursement.service.ShuiWenWaterLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Java
 * @date 2024-03-14 15:59:24
 **/
@Service
public class ShuiWenWaterLevelServiceImpl extends BaseMybatisServiceImpl<ShuiWenWaterLevel,Long> implements ShuiWenWaterLevelService {

    @Autowired
    private ShuiWenWaterLevelMapper shuiWenWaterLevelMapper;


    @Override
    protected BaseMapper<ShuiWenWaterLevel, Long> getBaseMapper() {
        return shuiWenWaterLevelMapper;
    }

    @Override
    public ShuiWenWaterLevel queryByDate(String format) {
        return shuiWenWaterLevelMapper.queryByDate(format);
    }
}
