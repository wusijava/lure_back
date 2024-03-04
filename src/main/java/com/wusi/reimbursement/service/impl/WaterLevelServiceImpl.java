package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.WaterLevel;
import com.wusi.reimbursement.mapper.WaterLevelMapper;
import com.wusi.reimbursement.service.WaterLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-05-18 09:18:16
 **/
@Service
public class WaterLevelServiceImpl extends BaseMybatisServiceImpl<WaterLevel,Long> implements WaterLevelService {

    @Autowired
    private WaterLevelMapper waterLevelMapper;


    @Override
    protected BaseMapper<WaterLevel, Long> getBaseMapper() {
        return waterLevelMapper;
    }

    @Override
    public WaterLevel queryByTime(String createTime) {
        return waterLevelMapper.queryByTime(createTime);
    }

}
