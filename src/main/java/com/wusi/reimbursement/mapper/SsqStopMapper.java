package com.wusi.reimbursement.mapper;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.entity.SsqStop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author admin
 * @date 2021-02-01 10:06:36
 **/
@Mapper
public interface SsqStopMapper extends BaseMapper<SsqStop,Long> {

List<SsqStop> queryAll();
}
