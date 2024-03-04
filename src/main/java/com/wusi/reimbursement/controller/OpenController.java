package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.entity.ExcelDto;
import com.wusi.reimbursement.utils.PoiUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ Description   :  通用controller
 * @ Author        :  wusi
 * @ CreateDate    :  2020/1/10$ 10:21$
 */
@Slf4j
@RestController
public class OpenController {
    @GetMapping(value = "/downloadExcel/{key}")
    @ResponseBody
    public void downloadExcel(@PathVariable(value = "key") String key, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object object = RedisUtil.get(key);
        ExcelDto dto = object == null ? null : (ExcelDto) object;
        if (dto == null) {
            log.error("key已经过期:{}", key);
            return;
        }
        PoiUtil.batchExport(dto.getHeaders(), dto.getKeys(), dto.getObjectList(), request, response);
        RedisUtil.del(key);
    }
}
