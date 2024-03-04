package com.wusi.reimbursement.service;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @ Description   :  java类作用描述
 * @ Author        :  wusi
 * @ CreateDate    :  2020/11/5$ 16:19$
 */
public class CglibProxyFactory {
    public  static Object getProxy(Class<?> clazz){
        //创建动态代理增强类
        Enhancer enhancer= new Enhancer();
        //设置类加载器
        enhancer.setClassLoader(clazz.getClassLoader());
        //设置被代理类
        enhancer.setSuperclass(clazz);
        //设置方法拦截
        enhancer.setCallback(new DebugMethodInterceptor());
        return enhancer.create();


    }
}
