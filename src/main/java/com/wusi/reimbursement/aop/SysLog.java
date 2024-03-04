package com.wusi.reimbursement.aop;

import java.lang.annotation.*;

/**
 * @ Description   :  操作日志自定义注解
 * @ Author        :  wusi
 * @ CreateDate    :  2020/5/29$ 11:17$
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "未命名方法";

}
