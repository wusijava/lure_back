package com.wusi.reimbursement;

import cn.hutool.core.date.DateTime;
import com.wusi.reimbursement.entity.ClassPlan;
import com.wusi.reimbursement.entity.SsqHistory;
import com.wusi.reimbursement.service.ClassPlanService;
import com.wusi.reimbursement.service.SsqHistoryService;
import com.wusi.reimbursement.utils.DateUtil;
import com.wusi.reimbursement.utils.MoneyUtil;
import com.wusi.reimbursement.utils.RedisUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * @ Description   :  获取双色球开奖号码
 * @ Author        :  wusi
 * @ CreateDate    :  2021/1/4$ 16:13$
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ssq {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static StringBuffer mStringBuffer;
    @Autowired
    private SsqHistoryService SsqHistoryService;
    @Autowired
    private ClassPlanService classPlanService;
    @Test
    public void getWater() throws IOException {
        String html = Jsoup.connect("https://cj.msa.gov.cn/xxgk/xxgkml/aqxx/swgg/202105/t20210511_677351.shtml").execute().body();
        Integer indexHan=html.indexOf("汉<");
        String levle=html.substring(indexHan+739, indexHan+744);
        System.out.println(levle);


    }
    @Test
    public void countDays() throws ParseException {
        int days = (int)(DateUtil.betweenDays(sdf.parse("2019-10-22"), new Date()));
        System.out.println(days);
    }
    @Test
    public void timeToStamp() throws ParseException {
        String str="2021-04-04"+" 00:00:01";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = sf.parse(str);
        long time = parse.getTime();
        System.out.println(time);
    }
    @Test
    public void testMath(){
        BigDecimal one=new BigDecimal("3");
        BigDecimal two=new BigDecimal("7");
        BigDecimal three=new BigDecimal("100");
        BigDecimal divide = one.divide(two, 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply = divide.multiply(three);
        System.out.println(multiply.setScale(2));

    }
    @Test
    public void Test() throws IOException {
        String html = Jsoup.connect("https://shuangseqiu.cjcp.com.cn/").timeout(200000).execute().body();
        //System.out.println(html);
        List<String> list=GetJsonValue(html, "<div class=\"red_q\">");
        System.out.println(html);
    }
    public static List<String> GetJsonValue(String jsonStr, String key) {
        List<String> ball=new ArrayList<>();
        //获取期数
        int index = jsonStr.indexOf("中国福利彩票双色球第");
        String term=jsonStr.substring(index+"中国福利彩票双色球第".length(), index+"中国福利彩票双色球第".length()+7);
        //开奖日期
        int bonusIndex = jsonStr.indexOf("开奖日期:");
        String bonusTime=jsonStr.substring(bonusIndex+"开奖日期:".length(), bonusIndex+"开奖日期:".length()+"2021-03-07".length());
        System.out.println(term);
        System.out.println(bonusTime);
        for(int i=0;i<6;i++){
            String value=getStr(jsonStr,key);
            ball.add(value);
            jsonStr=jsonStr.replace(key+value, value);
        }
        String blue=getStr(jsonStr,"<div class=\"blue_q\">");
        ball.add(blue);
        return ball;
    }
    public static String getStr(String jons,String div){
        int index = jons.indexOf(div);
        int length = div.length();
        return jons.substring(index + length, index + length+2);
    }
    @Test
    public void getNum() throws ParseException {
      /*  String ssqNum = WeekUtils.getSsqNum();
        System.out.println(ssqNum);*/
       boolean boo= Integer.valueOf("03")>2;
        System.out.println(boo);
    }
    @Test
    public void getAllNum(){

        System.out.println("正在获取...");
        mStringBuffer = new StringBuffer();
        String baseUrlPrefix = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_";
        String baseUrlSuffix = ".html";
        String homeUrl = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_1.html";
        String pageCountContent = getHtmlString(homeUrl);
        int pageCount = getPageCount(pageCountContent);
        if (pageCount > 0) {
            for (int i = 1; i <= pageCount; i++) {
                String url = baseUrlPrefix + i + baseUrlSuffix;
                String pageContent = getHtmlString(url);
                if (pageContent != null && !pageContent.equals("")) {
                    List<SsqHistory> oneTermContent = getOneTermContent(pageContent);
                    SsqHistoryService.insertBatch(oneTermContent);
                } else {
                    System.out.println("第" + i + "页丢失");
                }
                try {
                    Thread.sleep(1200);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
          /*  File file = new File("双色球.txt");
            if (file.exists()) {
                file.delete();
            }
            try {
                FileWriter writer = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(mStringBuffer.toString());
                bufferedWriter.close();
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //BufferedWriter writer = new BufferedWriter(new OutputS)*/
        } else {
            System.out.println("结果页数为0");
        }
        System.out.println("完成！");
    }
    /**
     * 获取网页源码
     * @return
     */
    private static String getHtmlString(String targetUrl) {
        String content = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows 7)");
            connection.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*");
            connection.setRequestProperty("Accept-Language", "zh-cn");
            connection.setRequestProperty("UA-CPU", "x86");
            //为什么没有deflate呢
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Content-type", "text/html");
            //keep-Alive，有什么用呢，你不是在访问网站，你是在采集。嘿嘿。减轻别人的压力，也是减轻自己。
            connection.setRequestProperty("Connection", "close");
            //不要用cache，用了也没有什么用，因为我们不会经常对一个链接频繁访问。（针对程序）
            connection.setUseCaches(false);
            connection.setConnectTimeout(6 * 1000);
            connection.setReadTimeout(6 * 1000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Charset", "utf-8");
            connection.connect();
            if (200 == connection.getResponseCode()) {
                InputStream inputStream = null;
                if (connection.getContentEncoding() != null && !connection.getContentEncoding().equals("")) {
                    String encode = connection.getContentEncoding().toLowerCase();
                    if (encode != null && !encode.equals("") && encode.indexOf("gzip") >= 0) {
                        inputStream = new GZIPInputStream(connection.getInputStream());
                    }
                }
                if (null == inputStream) {
                    inputStream = connection.getInputStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                content = builder.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return content;
    }
    private static List<SsqHistory> getOneTermContent(String pageContent) {
        String regex = "<td align=\"center\" style=\"padding-left:10px;\">[\\s\\S]+?</em></td>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pageContent);
        List<SsqHistory> list=new ArrayList<>();
        while (matcher.find()) {
            String oneTermContent = matcher.group();
            list.add(getOneTermNumbers(oneTermContent,pageContent));
        }
        return list;
    }
    private static SsqHistory getOneTermNumbers(String oneTermContent,String pageContent) {
        int i= pageContent.indexOf(oneTermContent);
        String time=pageContent.substring(i-68, i-58);
        String term=pageContent.substring(i-23, i-19)+"-"+pageContent.substring(i-19, i-16);
        SsqHistory his=new SsqHistory();
        String regex = ">\\d+<";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(oneTermContent);
        List<String> ssq=new ArrayList<>();
        while (matcher.find()) {
            String content = matcher.group();
            String ballNumber = content.substring(1, content.length()-1);
            ssq.add(ballNumber);
            mStringBuffer.append(ballNumber).append(" ");
        }
        his.setTerm(term);
        his.setRed1(ssq.get(0));
        his.setRed2(ssq.get(1));
        his.setRed3(ssq.get(2));
        his.setRed4(ssq.get(3));
        his.setRed5(ssq.get(4));
        his.setRed6(ssq.get(5));
        his.setBlue(ssq.get(6));
        his.setBonusTime(time);
        his.setCreateTime(new Date());
        return his;
        //mStringBuffer.append("\r\n");
    }
    /**
     * 获取总页数
     * @param result
     */
    private static int getPageCount(String result) {
        String regex = "\\d+\">末页";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        String[] splits = null;
        while (matcher.find()) {
            String content = matcher.group();
            splits = content.split("\"");
            break;
        }
        if (splits != null && splits.length == 2) {
            String countString = splits[0];
            if (countString != null && !countString.equals("")) {
                return Integer.parseInt(countString);
            }
        }
        return 0;
    }

    @Test
    public void getTimes(){
        int num1=0, num2=0,num3=0,num4=0,num5=0,num6=0,num7=0,num8=0,num9=0,num10=0,num11=0,num12=0,num13=0,
                num14=0,num15=0,num16=0,num17=0,num18=0,num19=0,num20=0,num21=0,num22=0,num23=0,num24=0,
                num25=0,num26=0,num27=0,num28=0,num29=0,num30=0,num31=0,num32=0,num33=0;
        List<SsqHistory> list = SsqHistoryService.queryList(new SsqHistory());
        for(SsqHistory his:list){
            for(int i=1;i<=33;i++){
                String str=null;
                if(i<10){
                    str="0"+String.valueOf(i);
                }else{
                    str=String.valueOf(i) ;
                }
                if(str.equals(his.getRed1())||str.equals(his.getRed2())||str.equals(his.getRed3())||str.equals(his.getRed4())||str.equals(his.getRed5())||str.equals(his.getRed6())){
                        if(i==1){
                            num1++;
                        }
                    if(i==2){
                        num2++;
                    }
                    if(i==3){
                        num3++;
                    }
                    if(i==4){
                        num4++;
                    }
                    if(i==5){
                        num5++;
                    }
                    if(i==6){
                        num6++;
                    }
                    if(i==7){
                        num7++;
                    }
                    if(i==8){
                        num8++;
                    }
                    if(i==9){
                        num9++;
                    }
                    if(i==10){
                        num10++;
                    }


                    if(i==11){
                        num11++;
                    }
                    if(i==12){
                        num12++;
                    }
                    if(i==13){
                        num13++;
                    }
                    if(i==14){
                        num14++;
                    }
                    if(i==15){
                        num15++;
                    }
                    if(i==16){
                        num16++;
                    }
                    if(i==17){
                        num17++;
                    }
                    if(i==18){
                        num18++;
                    }
                    if(i==19){
                        num19++;
                    }
                    if(i==20){
                        num20++;
                    }


                    if(i==21){
                        num21++;
                    }
                    if(i==22){
                        num22++;
                    }
                    if(i==23){
                        num23++;
                    }
                    if(i==24){
                        num24++;
                    }
                    if(i==25){
                        num25++;
                    }
                    if(i==26){
                        num26++;
                    }
                    if(i==27){
                        num27++;
                    }
                    if(i==28){
                        num28++;
                    }
                    if(i==29){
                        num29++;
                    }
                    if(i==30){
                        num30++;
                    }
                    if(i==31){
                        num31++;
                    }
                    if(i==32){
                        num32++;
                    }
                    if(i==33){
                        num33++;
                    }
                }
            }
        }
        List<Integer> sorted=new ArrayList<>();
        sorted.add(num1);
        sorted.add(num2);sorted.add(num3);sorted.add(num4);sorted.add(num5);sorted.add(num6);sorted.add(num7);sorted.add(num8);
        sorted.add(num9);sorted.add(num10);sorted.add(num11);sorted.add(num12);sorted.add(num13);sorted.add(num14);sorted.add(num15);
        sorted.add(num16);sorted.add(num17);sorted.add(num18);sorted.add(num19);sorted.add(num20);sorted.add(num21);
        sorted.add(num22);sorted.add(num23);sorted.add(num24);sorted.add(num25);sorted.add(num26);sorted.add(num27);sorted.add(num28);
        sorted.add(num29);sorted.add(num30);sorted.add(num31);sorted.add(num32);sorted.add(num33);
        Collections.sort(sorted, (y, x)->{
            return x-y;
        });

        System.out.println(sorted);
        LinkedHashMap<String,Integer> six=new LinkedHashMap<String,Integer>();
        for(int j=32;j>26;j--){
            if(sorted.get(j)==num1){
                six.put("red1",num1);
            }
            if(sorted.get(j)==num2){
                six.put("red2",num2);
            }
            if(sorted.get(j)==num3){
                six.put("red3",num3);
            }
            if(sorted.get(j)==num4){
                six.put("red4",num4);
            }if(sorted.get(j)==num5){
                six.put("red5",num5);
            }
            if(sorted.get(j)==num6){
                six.put("red6",num6);
            }
            if(sorted.get(j)==num7){
                six.put("red7",num7);
            }
            if(sorted.get(j)==num8){
                six.put("red8",num8);
            }
            if(sorted.get(j)==num9){
                six.put("red9",num9);
            }
            if(sorted.get(j)==num10){
                six.put("red10",num10);
            }


            if(sorted.get(j)==num11){
                six.put("red11",num11);
            }
            if(sorted.get(j)==num12){
                six.put("red12",num12);
            }
            if(sorted.get(j)==num13){
                six.put("red13",num13);
            }
            if(sorted.get(j)==num14){
                six.put("red14",num14);
            }if(sorted.get(j)==num15){
                six.put("red15",num15);
            }
            if(sorted.get(j)==num16){
                six.put("red16",num16);
            }
            if(sorted.get(j)==num17){
                six.put("red17",num17);
            }
            if(sorted.get(j)==num18){
                six.put("red18",num18);
            }
            if(sorted.get(j)==num19){
                six.put("red19",num19);
            }
            if(sorted.get(j)==num20){
                six.put("red20",num20);
            }


            if(sorted.get(j)==num21){
                six.put("red21",num21);
            }
            if(sorted.get(j)==num22){
                six.put("red22",num22);
            }
            if(sorted.get(j)==num23){
                six.put("red23",num23);
            }
            if(sorted.get(j)==num24){
                six.put("red24",num24);
            }
            if(sorted.get(j)==num25){
                six.put("red25",num25);
            }
            if(sorted.get(j)==num26){
                six.put("red26",num26);
            }
            if(sorted.get(j)==num27){
                six.put("red27",num27);
            }
            if(sorted.get(j)==num28){
                six.put("red28",num28);
            }
            if(sorted.get(j)==num29){
                six.put("red29",num29);
            }
            if(sorted.get(j)==num30){
                six.put("red30",num30);
            }
            if(sorted.get(j)==num31){
                six.put("red31",num31);
            }
            if(sorted.get(j)==num32){
                six.put("red32",num32);
            }
            if(sorted.get(j)==num33){
                six.put("red33",num33);
            }

        }
        System.out.println(six);
    }
    @Test
    public void del(){
        RedisUtil.del("ti");

    }

}
