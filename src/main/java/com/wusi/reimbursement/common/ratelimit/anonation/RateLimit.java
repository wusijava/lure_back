package com.wusi.reimbursement.common.ratelimit.anonation;

import java.lang.annotation.*;

/**
 * @author wusi
 * @date 2019/12/31 17:50
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface RateLimit {

    double permitsPerSecond() default 1.0;

    boolean ipLimit() default false;

    String description()  default "";

}