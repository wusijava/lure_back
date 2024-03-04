package com.wusi.reimbursement.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description   :  图形验证
 * @ Author        :  wusi
 * @ CreateDate    :  2021/2/19$ 16:18$
 */
@RestController
@Slf4j
public class CaptController {
    /*@Autowired
    private GlobalCacheManager cacheManager;*/
    @Autowired
    @Lazy
    private CaptchaService defaultCaptchaServiceImpl;

    @PostMapping(value = "getCode")
    public ResponseModel getCode(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = null;
        try {
            if (DataUtil.isEmpty(captchaVO.getCaptchaType())) {
                return new ResponseModel();
            }
            responseModel = defaultCaptchaServiceImpl.get(captchaVO);
            return responseModel;
        } catch (Exception e) {
            log.error("获取图形验证码失败:{}", e);
            return responseModel;
        }
    }


    @PostMapping(value = "check")
    public ResponseModel checkCode(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = null;
        try {
            if (DataUtil.isEmpty(captchaVO.getCaptchaType())) {
                return new ResponseModel();
            }
            if (DataUtil.isEmpty(captchaVO.getPointJson())) {
                return new ResponseModel();
            }
            if (DataUtil.isEmpty(captchaVO.getToken())) {
                return new ResponseModel();
            }
            responseModel = defaultCaptchaServiceImpl.check(captchaVO);
            if (DataUtil.isNotEmpty(responseModel) && DataUtil.isNotEmpty(responseModel.getRepData())) {
                CaptchaVO repData = (CaptchaVO) responseModel.getRepData();
                //cacheManager.setCaptchaKey(repData.getToken(), 1);
                RedisUtil.set(repData.getToken(), 1, 180);
            }
            return responseModel;
        } catch (Exception e) {
            log.error("效验图形验证码失败:{}", e);
            return responseModel;
        }
    }
}
