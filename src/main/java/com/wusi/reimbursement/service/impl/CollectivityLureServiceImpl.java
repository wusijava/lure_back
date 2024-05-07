package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.CollectivityLure;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.mapper.CollectivityLureMapper;
import com.wusi.reimbursement.service.CollectivityLureService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.vo.CreateCollectivity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Java
 * @date 2024-04-30 22:10:52
 **/
@Service
public class CollectivityLureServiceImpl extends BaseMybatisServiceImpl<CollectivityLure,Long> implements CollectivityLureService {

    @Autowired
    private CollectivityLureMapper collectivityLureMapper;


    @Override
    protected BaseMapper<CollectivityLure, Long> getBaseMapper() {
        return collectivityLureMapper;
    }

    @Override
    public void createCollectivity(RequestContext.RequestUser currentUser, CreateCollectivity data) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CollectivityLure collectivityLure=new CollectivityLure();
        collectivityLure.setActivityName(data.getActivityName());
        collectivityLure.setAddress(data.getAddress());
        collectivityLure.setCreateTime(new Date());
        collectivityLure.setSlogan(data.getSlogan());
        collectivityLure.setState(1);
        collectivityLure.setCreatorUid(currentUser.getUid());
        collectivityLure.setCreatorName(currentUser.getNickName());
        collectivityLure.setParticipantName(currentUser.getNickName());
        collectivityLure.setParticipantUid(currentUser.getUid());
        collectivityLure.setLureDate(sdf.parse(data.getLureDate()));
        collectivityLure.setRemark(data.getRemark());
        collectivityLureMapper.insert(collectivityLure);
    }

    @Override
    public void joinCollectivity(Long id) throws Exception {
        CollectivityLure collectivityLure = collectivityLureMapper.selectById(id);
        if(DataUtil.isEmpty(collectivityLure)){
            throw  new  Exception("未查询到此id活动");
        }
        collectivityLure.setParticipantName(collectivityLure.getParticipantName()+";"+RequestContext.getCurrentUser().getNickName());
        collectivityLure.setParticipantUid(collectivityLure.getParticipantUid()+";"+RequestContext.getCurrentUser().getUid());
        collectivityLureMapper.updateById(collectivityLure);
    }

    @Override
    public void quitCollectivity(Long id) throws Exception {
        CollectivityLure collectivityLure = collectivityLureMapper.selectById(id);
        if(DataUtil.isEmpty(collectivityLure)){
            throw  new  Exception("未查询到此id活动");
        }
        collectivityLure.setParticipantName(collectivityLure.getParticipantName().replace(";"+RequestContext.getCurrentUser().getNickName(), ""));
        collectivityLure.setParticipantUid(collectivityLure.getParticipantUid().replace(";"+RequestContext.getCurrentUser().getUid(), ""));
        collectivityLureMapper.updateById(collectivityLure);
    }
}
