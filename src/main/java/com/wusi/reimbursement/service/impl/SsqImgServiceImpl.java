package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.SsqImg;
import com.wusi.reimbursement.mapper.SsqImgMapper;
import com.wusi.reimbursement.service.SsqImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-01-29 11:00:40
 **/
@Service
public class SsqImgServiceImpl extends BaseMybatisServiceImpl<SsqImg,Long> implements SsqImgService {

    @Autowired
    private SsqImgMapper ssqImgMapper;


    @Override
    protected BaseMapper<SsqImg, Long> getBaseMapper() {
        return ssqImgMapper;
    }
}
