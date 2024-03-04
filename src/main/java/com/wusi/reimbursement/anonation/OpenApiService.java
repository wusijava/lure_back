package com.wusi.reimbursement.anonation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author wusi
 * @description 开放接口实现类注解
 * @date 2020-8-22 11:29:45
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface OpenApiService {
}
