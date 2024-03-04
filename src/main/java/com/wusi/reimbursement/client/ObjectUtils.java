package com.wusi.reimbursement.client;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wusi
 * @description 常见的辅助类
 * @date 2020-8-22 11:38:30
 */
public final class ObjectUtils {
    private ObjectUtils() {
    }


    /**
     * 判断对象是否Empty(null或元素为0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static final boolean isEmpty(Object pObj) {
        if (pObj == null) {
            return true;
        }
        if (pObj == "") {
            return true;

        }
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection<?>) {
            if (((Collection<?>) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map<?, ?>) {
            if (((Map<?, ?>) pObj).size() == 0) {
                return true;
            }
        } else if (pObj.getClass().isArray()) {
            if (Array.getLength(pObj) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素>0)<br>
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static final boolean isNotEmpty(Object pObj) {
        if (pObj == null) {
            return false;
        }
        if (pObj == "") {
            return false;
        }
        if (pObj instanceof String) {
            if (((String) pObj).trim().length() == 0) {
                return false;
            }
        } else if (pObj instanceof Collection<?>) {
            if (((Collection<?>) pObj).size() == 0) {
                return false;
            }
        } else if (pObj instanceof Map<?, ?>) {
            if (((Map<?, ?>) pObj).size() == 0) {
                return false;
            }
        } else if (pObj.getClass().isArray()) {
            if (Array.getLength(pObj) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 讲对象转换成Map
     *
     * @param obj 参数
     * @return Map
     */
    public static Map<String, String> toStringMap(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            if (value == null) {
                continue;
            }
            map.put(key, String.valueOf(value));
        }
        return map;
    }
}
