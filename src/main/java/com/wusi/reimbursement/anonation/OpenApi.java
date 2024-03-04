package com.wusi.reimbursement.anonation;


import com.wusi.reimbursement.client.ApiMethod;

import java.lang.annotation.*;

/**
 * @author wusi
 * @description 开放接口注解
 * @date 2020-8-22 11:29:45
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OpenApi {

    /**
     * 方法
     *
     * @return
     */
    ApiMethod api();

}
