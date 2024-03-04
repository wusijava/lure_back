package com.wusi.reimbursement.client;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class HttpUtils {

    /**
     * http请求socket连接超时时间,毫秒为单位
     */
    static final int HTTP_SOCKET_TIMEOUT = 30000;

    /**
     * http请求连接超时时间,毫秒为单位
     */
    static final int HTTP_CONNECT_TIMEOUT = 30000;

    static RestTemplate template;

    static {
        template = new RestTemplate(getClientHttpRequestFactory());
    }


    /**
     * 配置HttpClient超时时间
     *
     * @return
     */
    static ClientHttpRequestFactory getClientHttpRequestFactory() {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTP_SOCKET_TIMEOUT)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT).build();
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    /**
     * post 请求
     *
     * @param param
     * @param url
     */
    public static String post(ApiRequestParam param, String url) {
        try {
            MultiValueMap<String, Object> postParameters = objectToMultiValueMap(param);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
            return template.postForObject(url, r, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static MultiValueMap<String, Object> objectToMultiValueMap(Object  obj) throws Exception {
        if (obj == null) {
            return null;
        }
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.add(key, value);
        }
        return map;
    }

}
