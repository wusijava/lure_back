package com.wusi.reimbursement.service;

import com.wusi.reimbursement.base.service.BaseService;
import com.wusi.reimbursement.entity.CollectivityLure;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.vo.CreateCollectivity;

import java.text.ParseException;

/**
 * @author Java
 * @date 2024-04-30 22:10:52
 **/
public interface CollectivityLureService extends BaseService<CollectivityLure,Long> {


    void createCollectivity(RequestContext.RequestUser currentUser, CreateCollectivity data) throws ParseException;

    void joinCollectivity(Long id) throws Exception;

    void quitCollectivity(Long id) throws Exception;
}
