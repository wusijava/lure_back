package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.DingDingTalkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 吴思  钉钉群发消息
 */

@RestController
@Slf4j
public class DingDingController {
    @RequestMapping(value = "sendDingDingMessage")
    public Response sendDingDingMessage(String content)  {
        if(DataUtil.isEmpty(content)){
            return Response.fail("内容不能为空~~");
        }
        try {
            DingDingTalkUtils.sendDingDingMsg(content);
        } catch (Exception e) {
            log.error("群发异常,{}", content);
            return Response.ok("群发失败!");
        }
        return Response.ok("群发成功!");
    }

}
