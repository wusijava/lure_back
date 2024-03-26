package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.ShuiWenWaterLevel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Java
 * @date 2024-03-14 15:59:24
 **/
@Mapper
public interface ShuiWenWaterLevelMapper extends BaseMapper<ShuiWenWaterLevel,Long> {


    ShuiWenWaterLevel queryByDate(String format);

    ShuiWenWaterLevel queryLastOne();
}
