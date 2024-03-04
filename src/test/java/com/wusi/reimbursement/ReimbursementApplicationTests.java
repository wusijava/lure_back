package com.wusi.reimbursement;

import com.wusi.reimbursement.entity.SellLog;
import com.wusi.reimbursement.entity.UserTest;
import com.wusi.reimbursement.query.SellLogQuery;
import com.wusi.reimbursement.service.SellLogService;
import com.wusi.reimbursement.utils.DataUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootTest

class ReimbursementApplicationTests {
    @Autowired
    private SellLogService sellLogService;

    @Test
    void contextLoads() {
    }

    @Test
    void testLambda() {
        List<SellLog> sellLogs = sellLogService.queryList(new SellLogQuery());
        //long num = sellLogs.stream().filter(a -> (DataUtil.isNotEmpty(a.getBuyerName())) && a.getBuyerName().indexOf("张") != -1).count();
        //System.out.println(num);
      /*  List<Integer> list= Arrays.asList(1,2,3,4);
        System.out.println(list);*/
        sellLogs.stream().filter(a->(a.getProfit()!=null)&&(Double.parseDouble(a.getProfit())>7000)).forEach((p)-> System.out.println(p.getRemark()+"----"+p.getProfit()));
        ConcurrentHashMap con=new ConcurrentHashMap();
    }

    @Test
    int getMax(int num) {
        StringBuilder sb=new StringBuilder(String.valueOf(num));
       int i = sb.indexOf("6");
        StringBuilder replace=null;
       if(i!=-1){
           return Integer.parseInt(sb.replace(i, i + 1, "9").toString());
       }else{
           return  num;
       }

    }
    @Test
    void test(){
        //System.out.println(getMax(666666666));
    }
    @Test
    @Transactional(rollbackFor = Exception.class)
    void TestPass() throws NoSuchMethodException {
        LinkedList<String> linkedList =new LinkedList();
        for (int i=0;i<10;i++){
            linkedList.add("str"+i);
        }
        linkedList.add(0, "str0");
        System.out.println(linkedList);
    /*    int[] a= new int[3];
        a[0]=0;
        a[1]=1;
        a[2]=2;
        int[] b=Arrays.copyOf(a, 10);
        System.out.println("----------------------"+b);
        for (int i=0;i<b.length;i++){
            System.out.println(b[i]);
        }
        System.out.println("----------------------"+b.length);*/

       /* List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; ++i) {
            list.add(i);
        }
        for (int i = 1; i <= 10; ++i) {
            list.add(i);
        }
        System.out.println(list);
        List<Integer> integerList=list.stream().distinct().collect(Collectors.toList());
        System.out.println(integerList);*/
        /*List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; ++i) {
            list.add(i);
        }
        list.removeIf(filter->filter%2==0);
        System.out.println(list);*/
       /* List list= new ArrayList();
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        //Collections.sort(names,(String a,String b)->  a.compareTo(b));
        names.sort((a,b)->a.compareTo(b));
        System.out.println(names);*/
        /*List<String> list= new ArrayList<>();
        list.add("1");
        list.add("2");
      for(String str:list){
          if("2".equals(str)){
            list.remove(str);
          }
      }
        System.out.println(list);*/
        /*List list=Arrays.asList(1,2,3,4);

        List list1= Lists.newArrayList(1,2,3);
        list1.add(5);
        System.out.println(list.size());//4*/
       /* UserTest t=new UserTest();
        boolean equals = Objects.equals(t.getAddress(), "a");

        System.out.println(equals);*/
        /*String str = "a,b,c, ,";
        String[] ary = str.split(",");
        // 预期大于 3，结果是 3
        System.out.println(ary.length);
        String s = ary[ary.length - 1];
        System.out.println(s);*/
        //System.out.println(3|9);
        //big();
        /*Map<Integer,String> map =new HashMap<>();
        map.put(1, "java");
        map.put(2, "c++");
        map.put(3, "PHP");
        Iterator<Map.Entry<Integer,String>> iterator=map.entrySet().iterator();*/
        /*while(){

        }*/
      /*  List<Integer> list=new ArrayList<>();
        list.add(2);
        //list.add("test");
        Class<? extends  List> clazz=list.getClass();

        Method add=clazz.getDeclaredMethod("add", Object.class);
        try {
            add.invoke(list, "kl");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(list);*/

    }
    void big(){

        /*SellLog log=new SellLog();
        log.setProfit("100");
        sellLogService.insert(log);
        System.out.println(log);
        int a= 100/0;*/

    }
}
