package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.PasswordRecord;
import com.wusi.reimbursement.entity.RequestContext;
import com.wusi.reimbursement.query.PasswordRecordQuery;
import com.wusi.reimbursement.service.OperateInfoService;
import com.wusi.reimbursement.service.PasswordRecordService;
import com.wusi.reimbursement.utils.AesUtil;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.vo.PasswordRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ Description   :  密码
 * @ Author        :  wusi
 * @ CreateDate    :  2021/4/14$ 14:43$
 */
@RestController
@RequestMapping(value = "api/password")
public class PasswordController {
    @Autowired
    private PasswordRecordService passwordRecordService;
    @Autowired
    private OperateInfoService operateInfoService;
    static SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.PATTERN_YYYY_MM_DD);
    @RequestMapping(value = "list")
    public Response<Page<PasswordRecordVo>> list(PasswordRecordQuery query) {
        RequestContext.RequestUser user = RequestContext.getCurrentUser();
        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        if (DataUtil.isEmpty(query.getLimit())) {
            query.setLimit(10);
        }
        query.setUser(user.getUsername());
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<PasswordRecord> page = passwordRecordService.queryPage(query, pageable);
        List<PasswordRecordVo> voList = new ArrayList<>();
        for (PasswordRecord record : page.getContent()) {
            voList.add(getListVo(record));
        }
        Page<PasswordRecordVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private PasswordRecordVo getListVo(PasswordRecord record) {
        PasswordRecordVo vo=new PasswordRecordVo();
        vo.setId(record.getId());
        vo.setAcount(record.getAcount());
        vo.setCreateTime(sdf.format(record.getCreateTime()));
        return vo;
    }

    @RequestMapping(value = "save")
    public Response<String> save(String acount,String pwd,String pwdAgain){
        if(DataUtil.isEmpty(acount)||DataUtil.isEmpty(pwd)||DataUtil.isEmpty(pwdAgain)){
            return Response.fail("参数不齐全!");
        }
        if(!pwd.trim().equals(pwdAgain.trim())){
            return Response.fail("两次密码不一致!");
        }
        RequestContext.RequestUser user = RequestContext.getCurrentUser();
        PasswordRecordQuery query=new PasswordRecordQuery();
        query.setAcount(acount);
        query.setUser(user.getUsername());
        Long aLong = passwordRecordService.queryCount(query);
        if(aLong!=0){
            return Response.fail("已存在此账号记录!");
        }
        PasswordRecord save=new PasswordRecord();
        save.setAcount(acount);
        save.setCreateTime(new Date());
        save.setUser(user.getUsername());
        save.setPassword(AesUtil.Encrypt(pwd, "12345678qwertyui"));
        passwordRecordService.insert(save);
        return Response.ok("新增成功!");
    }

    @RequestMapping(value = "showPassword")
    public Response<String > showPassword(String pwd,Long rowId){
        if(DataUtil.isEmpty(pwd)||DataUtil.isEmpty(rowId)){
            return Response.fail("参数不齐全!");
        }
        String result = operateInfoService.verifyPwd(pwd);
        if(DataUtil.isNotEmpty(result)){
            return Response.fail(result);
        }
        PasswordRecord passwordRecord = passwordRecordService.queryById(rowId);
        if(DataUtil.isEmpty(passwordRecord)){
            return Response.fail("无此记录!");
        }
        return Response.ok(AesUtil.Decrypt(passwordRecord.getPassword(),"12345678qwertyui"));
    }
    @RequestMapping(value = "deletePassword")
    public Response<String> delPassword(Long id){
        if(DataUtil.isEmpty(id)){
            return Response.fail("id为空!");
        }
        passwordRecordService.deleteByID(id);
        return Response.ok("删除成功!");
    }
}
