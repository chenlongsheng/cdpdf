///**
// * 
// */
//package com.jeeplus.modules.warm.web;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URISyntaxException;
//
//import org.apache.commons.httpclient.util.HttpURLConnection;
//import org.jboss.netty.channel.ChannelHandler.Sharable;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.jeeplus.common.persistence.MapEntity;
//import com.jeeplus.common.web.BaseController;
//import com.rabbitmq.http.client.Client;
//import com.adobe.xmp.impl.Base64;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import sun.misc.BASE64Encoder;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * 数据配置Controller
// * 
// * @author long
// * @version 2018-07-24
// */
//@Controller
//public class rabbitmqController extends BaseController {
//
//    public String getData() {
//
//        try {
//
////            URL url = new URL(
////                    "http://cdpdf-server.cdsoft.cn/rabbitmq/api/queues?page=1&page_size=500&name=dev.command&use_regex=false&pagination=true");
//
//            URL url = new URL(
//                    "http://cdpdf-server.cdsoft.cn/rabbitmq/api/queues?name=dev.command&use_regex=false&pagination=true");
//            URLConnection uc = url.openConnection();
//            String encoded = new String(Base64.encode(new String("cdsoft:Ps56jw2hIjPOuTnh").getBytes()));
//            uc.setRequestProperty("Authorization", "Basic " + encoded);
//            uc.connect();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(uc.getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
////                System.out.println(line);
//                return line;
//            }
//            rd.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//
//    public void test() {
//
//        JSONObject jsonObj = JSON.parseObject(getData());
//        JSONArray ja = jsonObj.getJSONArray("items");
//        System.out.println(ja.size());
//        for (int i = 0; i < ja.size(); i++) {
//            MapEntity program = JSONObject.parseObject(ja.get(i).toString(), MapEntity.class);
//            String name = program.get("name").toString();
//            System.out.println("...." + name);
//
//        }
//
//    }
//
////  @Scheduled(cron="0 */10 * * * ?")
//    @Scheduled(fixedRate=5000)
//    public void test2() {
//
////        List<String> test1 = test1();
//        System.out.println("实际总共..");
////        for (String s : test1) {
////            System.out.println(s);
////        }
//
//    }
//
//    public List<String> test1() {
//
//        List<String> list = new ArrayList<String>();
//        JSONArray ja = JSON.parseArray(getData());
//        System.out.println("接收总共:" + ja.size());
//        for (int i = 0; i < ja.size(); i++) {
//            MapEntity program = JSONObject.parseObject(ja.get(i).toString(), MapEntity.class);
//            String name = program.get("name").toString();
////            System.out.println("xxxxxx" + name);
//            String[] sp = name.split("\\.");
//
//            if (sp.length >= 4) {
//                if ((sp[0] + sp[1]).equals("devcommand")) {
//                    if (sp[2].equals("sn")) {
//                        list.add(sp[3]);
//                    } else {
//                        list.add(sp[2]);
////                        System.out.println("====" + name);
//                    }
//
//                }
//
//            }
//
//        }
//        return list;
//
//    }
//}
