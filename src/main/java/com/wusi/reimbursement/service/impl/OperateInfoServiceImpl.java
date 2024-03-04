package com.wusi.reimbursement.service.impl;

import com.wusi.reimbursement.base.dao.mybatis.BaseMapper;
import com.wusi.reimbursement.base.service.impl.BaseMybatisServiceImpl;
import com.wusi.reimbursement.entity.OperateInfo;
import com.wusi.reimbursement.mapper.OperateInfoMapper;
import com.wusi.reimbursement.service.OperateInfoService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.PassWordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author admin
 * @date 2021-04-14 14:58:07
 **/
@Service
public class OperateInfoServiceImpl extends BaseMybatisServiceImpl<OperateInfo,Long> implements OperateInfoService {

    @Autowired
    private OperateInfoMapper operateInfoMapper;
    @Value("${operateInfo.salt}")
    private String salt;

    @Override
    protected BaseMapper<OperateInfo, Long> getBaseMapper() {
        return operateInfoMapper;
    }

    @Override
    public String verifyPwd(String pwd) {
        OperateInfo query = new OperateInfo();
        String password = PassWordUtil.generatePasswordSha1WithSalt(pwd, salt);
        query.setPassword(password);
        OperateInfo operateInfo = this.queryOne(query);
        if (DataUtil.isEmpty(operateInfo)) {
            return "密码错误";
        }
        if (!password.equals(operateInfo.getPassword())) {
            return "请输入正确的密码";
        }
        if (!operateInfo.getState().equals(OperateInfo.State.OPEN.getCode())) {
            return "账号已冻结,无法操作!";
        }
        return null;
    }
}
