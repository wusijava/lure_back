package com.wusi.reimbursement.utils;

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

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpUtils {

    /**
     * http请求socket连接超时时间,毫秒为单位
     */
    static final int HTTP_SOCKET_TIMEOUT = 10000;

    /**
     * http请求连接超时时间,毫秒为单位
     */
    static final int HTTP_CONNECT_TIMEOUT = 10000;

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
     * 推送异步通知
     *
     * @param params
     * @param url
     */
    public static String sendPost(Map<String, String> params, String url) {
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        for (String key : params.keySet()) {
            postParameters.add(key, params.get(key));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        String result = template.postForObject(url, r, String.class);
        return result;
    }

    /**
     * 获取请求里的参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration request_BodyNames = request.getParameterNames();
        while (request_BodyNames.hasMoreElements()) {
            String name = (String) request_BodyNames.nextElement();
            String value = request.getParameter(name);
            map.put(name, value);
        }
        return map;
    }

    /**
     * 获取请求里的参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
}
