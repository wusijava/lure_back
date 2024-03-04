package com.wusi.reimbursement.controller;

import com.wusi.reimbursement.common.Response;
import com.wusi.reimbursement.entity.Festival;
import com.wusi.reimbursement.query.LureFishGetQuery;
import com.wusi.reimbursement.service.FestivalService;
import com.wusi.reimbursement.service.LureFishGetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * @ Description   :  天行数据接口测试
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/29$ 15:44$
 */
@RestController
@Slf4j
public class TianApiController {
    @Autowired
    private FestivalService festivalService;
    //key
    private static String httpArg = "key=7e110e1235a562e9e11b09cfd965c5a2";
    //毒鸡汤
    private static String duJiTang = "http://api.tianapi.com/txapi/dujitang/index";
    //每日一句
    private static String dayEnglish = "http://api.tianapi.com/txapi/everyday/index";
    //网易云
    private static String wangYi = "http://api.tianapi.com/txapi/hotreview/index";
    //神回复
    private static String huiFu="http://api.tianapi.com/txapi/godreply/index";
    //最美宋词
    private static String songCi="http://api.tianapi.com/txapi/zmsc/index";
    //台词
    private static String dialogue="http://api.tianapi.com/txapi/dialogue/index";
    @Autowired
    private LureFishGetService lureFishGetService;
    @RequestMapping(value = "du", produces = "application/json; charset=utf-8")
    public String duJiTang() {
        String jsonResult = request(duJiTang, httpArg);
        return jsonResult;
    }

    private static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "english", produces = "application/json; charset=utf-8")
    public String english() {
        String jsonResult = request(dayEnglish, httpArg);
        return jsonResult;
    }

    @RequestMapping(value = "wangYi", produces = "application/json; charset=utf-8")
    public String wangYi() {
        String jsonResult = request(wangYi, httpArg);
        return jsonResult;
    }
    @RequestMapping(value = "huiFu", produces = "application/json; charset=utf-8")
    public String huiFu() {
        String jsonResult = request(huiFu, httpArg);
        return jsonResult;
    }
    @RequestMapping(value = "songCi", produces = "application/json; charset=utf-8")
    public String songCi() {
        String jsonResult = request(songCi, httpArg);
        return jsonResult;
    }
    @RequestMapping(value = "dialogue", produces = "application/json; charset=utf-8")
    public String dialogue() {
        String jsonResult = request(dialogue, httpArg);
        return jsonResult;
    }
    @RequestMapping(value = "getTime", produces = "application/json; charset=utf-8")
    public List<Festival> getTime() {
        List<Festival> festivals = festivalService.queryList(new Festival());
        LureFishGetQuery query=new LureFishGetQuery();
        query.setGetFish(1);
        festivals.get(1).setTime(lureFishGetService.queryOne(query).getCreateTime().getTime());
        return festivals;
    }

}
