package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.aop.SysLog;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.common.ratelimit.anonation.RateLimit;
import com.wusi.reimbursement.service.UploadService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Map;



@RestController
@RequestMapping(value = "/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;


    @RateLimit(permitsPerSecond = 0.5, ipLimit = true,description = "限制访问Token频率")
    @PostMapping(value = "getToken")
    public Response<Map<String, Object>> getUploadToken(String type) {
        if (DataUtil.isNotEmpty(type)) {
            String filename = MessageFormat.format("temp/{0}/{1}", type, StringUtils.createRandom(false, 6));
            Map<String, Object> result = uploadService.getUploadToken(filename + ".jpg");
            if (!DataUtil.isEmpty(result)) {
                return Response.ok(result);
            } else {
                return Response.fail("上传失败");
            }
        } else {
            return Response.fail("资料不全");
        }
    }

}
