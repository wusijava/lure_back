package com.wusi.reimbursement.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.entity.Weather;
import com.wusi.reimbursement.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * @ Description   :  获取天气工具类
 * @ Author        :  wusi
 * @ CreateDate    :  2019/11/28$ 16:13$
 */
public class WeatherUtils {
    public static Weather getWeather(String location) throws Exception {
        if (DataUtil.isEmpty(location)) {
            location = "wuhan";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        Weather data = new Weather();
        String url = "https://devapi.qweather.com/v7/weather/3d?location=" + location + "&key=b941bbcd687b486aa07aab8586dc115e";
        RestTemplate restTemplate = new RestTemplateBuilder().requestFactory(OkHttp3ClientHttpRequestFactory.class)
                .setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(30)).build();
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = converters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof MappingJackson2XmlHttpMessageConverter) {
                // 删除xml序列化，内部微服务之间用json交互
                iterator.remove();
            } else if (converter instanceof StringHttpMessageConverter) {
                // 解决使用restTemplate中文乱码的问题
                ((StringHttpMessageConverter) converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
        
        /*List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.stream().forEach(httpMessageConverter -> {
                        StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                        messageConverter.setDefaultCharset(Charset.forName("UTF-8"));
                    //发送请求

                });*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class);
        System.out.println(response.getBody());
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(response.getBody());
        //获取第二层数据
        String daily = jsonObject.getJSONArray("daily").get(0).toString();
        JSONObject jj = JSONObject.parseObject(daily);
        data.setCondTxtDay(jj.getString("textDay"));
        data.setCondTxtNight(jj.getString("textNight"));
        data.setDate(sdf.parse(jj.getString("fxDate") + " 00:00:00"));
        data.setHum(jj.getString("humidity"));
        data.setMoonRise(jj.getString("moonrise"));
        data.setMoonSet(jj.getString("moonset"));
        data.setPres(Integer.valueOf(jj.getString("pressure")));
        data.setSunRise(jj.getString("sunrise"));
        data.setSunSet(jj.getString("sunset"));
        data.setTmpMax(jj.getString("tempMax"));
        data.setTmpMin(jj.getString("tempMin"));
        data.setWindDir(jj.getString("windDirDay"));
        data.setWindSc(jj.getString("windScaleDay"));
        data.setWindSpd(jj.getString("windSpeedDay"));
        data.setCreateTime(new Date());
        data.setLoc(jj.getString("fxDate") + " 00:00:00");
        return data;
    }


    public static void main(String[] args) throws Exception {
        getWeather("114.186769,30.453926");
    }
}
