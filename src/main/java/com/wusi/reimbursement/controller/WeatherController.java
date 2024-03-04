package com.wusi.reimbursement.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.Weather;
import com.wusi.reimbursement.query.WeatherQuery;
import com.wusi.reimbursement.service.WeatherService;
import com.wusi.reimbursement.utils.DataUtil;
import com.wusi.reimbursement.utils.WeatherUtils;
import com.wusi.reimbursement.vo.WeatherVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Description   :  天气提醒
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/5$ 17:50$
 */
@RestController
@Slf4j
@RequestMapping(value = "api/open/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Scheduled(cron = "0 0 6 * * ?")
    @RequestMapping(value = "getWeather")
    public void sendWeatherSMS() throws Exception {
        Weather weather = WeatherUtils.getWeather("114.186769,30.453926");
        weather.setProvince("湖北省");
        weather.setCountry("中国");
        weather.setLocation("武汉");
        weather.setParentCity("武汉");
        log.error("三日内天气,{}", JSONObject.toJSONString(weather));
        weatherService.insert(weather);
    }
    @RequestMapping(value = "list")
    public Response weatherList(WeatherQuery query){

        if (DataUtil.isEmpty(query.getPage())) {
            query.setPage(0);
        }
        query.setLimit(1);
        Pageable pageable = PageRequest.of(query.getPage(), query.getLimit());
        Page<Weather> page = weatherService.queryPage(query, pageable);
        List<WeatherVo> voList = new ArrayList<>();
        for (Weather w : page.getContent()) {
            voList.add(getWeatherList(w));
        }
        Page<WeatherVo> voPage = new PageImpl<>(voList, pageable, page.getTotalElements());
        return Response.ok(voPage);
    }

    private WeatherVo getWeatherList(Weather w) {
        WeatherVo vo=new WeatherVo();
        vo.setDate(sdf.format(w.getDate()));
        vo.setLocation(w.getProvince()+"/"+w.getParentCity()+"市");
        vo.setLoc(w.getLoc());
        vo.setCondTxtDay(w.getCondTxtDay());
        vo.setCondTxtNight(w.getCondTxtNight());
        vo.setTemp(w.getTmpMin()+"度-"+w.getTmpMax()+"度");
        vo.setPres(w.getPres());
        vo.setSunRise(w.getSunRise());
        vo.setSunSet(w.getSunSet());
        vo.setHum(w.getHum());
        vo.setWindDir(w.getWindDir());
        vo.setWindSc(w.getWindSc());
        vo.setWindSpd(w.getWindSpd());
        return vo;
    }

}
