package com.wusi.reimbursement.utils;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.entity.Weather;
import com.wusi.reimbursement.vo.IndexWeatherAndWaterLevelVoNow;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @ Description   :  获取天气工具类
 * @ Author        :  wusi
 * @ CreateDate    :  2019/11/28$ 16:13$
 */
public class NewWeatherUtils {
    public static IndexWeatherAndWaterLevelVoNow getWeather(String location) throws Exception {
        if (DataUtil.isEmpty(location)) {
            location = "wuhan";
        }
        String date = null;
        Weather data = new Weather();
        String url = "https://devapi.qweather.com/v7/weather/now?location=" + location + "&key=b941bbcd687b486aa07aab8586dc115e";
        RestTemplate restTemplate = new RestTemplateBuilder().requestFactory(OkHttp3ClientHttpRequestFactory.class)
                .setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(30)).build();
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof MappingJackson2XmlHttpMessageConverter) {
                iterator.remove();
            } else if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        return JSONObject.parseObject(jsonObject.getString("now"), IndexWeatherAndWaterLevelVoNow.class);
    }


    public static void main(String[] args) throws Exception {
        IndexWeatherAndWaterLevelVoNow weather = getWeather("114.186769,30.453926");
        System.out.println(weather);
    }
}
