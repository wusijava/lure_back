package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.IllegalLog;
import com.wusi.reimbursement.mapper.IllegalLogMapper;
import com.wusi.reimbursement.service.IllegalLogService;
import com.wusi.reimbursement.utils.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.DateTimeAtCompleted;
import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @author Java
 * @date 2024-07-22 16:28:39
 **/
@Service
public class IllegalLogServiceImpl extends BaseMybatisServiceImpl<IllegalLog,Long> implements IllegalLogService {

    @Autowired
    private IllegalLogMapper illegalLogMapper;


    @Override
    protected BaseMapper<IllegalLog, Long> getBaseMapper() {
        return illegalLogMapper;
    }

    @Override
    public void saveIllegalLog(String uid, String userName, String content, Integer state, Integer source, String traceId,Integer type,Integer reason) {
        IllegalLog log=new IllegalLog();
        log.setContent(content);
        log.setUid(uid);
        log.setUserName(userName);
        log.setTraceId(traceId);
        log.setType(type);
        log.setCreateTime(new Date());
        log.setContent(content);
        log.setReason(getReason(reason));
        log.setState(state);
        log.setSource(source);
        illegalLogMapper.insert(log);
    }

    @Override
    public IllegalLog selectByTraceId(String traceId) {
        if(DataUtil.isEmpty(traceId)){
            return null;
        }
        return illegalLogMapper.selectByTraceId(traceId);
    }

    private String getReason(Integer reason) {
        switch (reason){
            case 100:
                return "正常";
            case 10001:
                return "广告";
            case 20001:
                return "时政";
            case 20002:
                return "色情";
            case 20003:
                return "辱骂";
            case 20006:
                return "违法犯罪";
            case 20008:
                return "欺诈";
            case 20012:
                return "低俗";
            case 20013:
                return "版权";
            case 21000:
                return "其他";
        }
        return "其他";

    }
}
