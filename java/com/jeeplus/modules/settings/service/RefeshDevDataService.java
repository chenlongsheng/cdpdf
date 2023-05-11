/**
 * 
 */
package com.jeeplus.modules.settings.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.adobe.xmp.impl.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.persistence.MapEntity;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.settings.dao.RefeshDevDataDao;

@Service
//@Lazy(false)
public class RefeshDevDataService {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RefeshDevDataDao refeshDevDataDao;

    @Value("${rabbitmq.username}")
    private String username;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.httpPath}")
    private String httpPath;

    int count = 0;

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

    Properties properties = new Properties();
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    public String getRabbitMqData() {

        try {
            String httpURL = httpPath;
            System.out.println("rabbitmq.httpPath: " + httpPath);

            URL url = new URL(httpURL);

            URLConnection uc = url.openConnection();
            System.out.println("rabbitmq.username: " + username);
            System.out.println("rabbitmq.password: " + password);

            String encoded = new String(Base64.encode(new String(username + ":" + password).getBytes()));
            uc.setRequestProperty("Authorization", "Basic " + encoded);
            uc.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
//                System.out.println(line);
                return line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<String> updateMqOnline() {
        List<String> listMq = new ArrayList<String>();
        JSONArray ja = JSON.parseArray(getRabbitMqData());
        System.out.println("接收总共:" + ja.size());
        for (int i = 0; i < ja.size(); i++) {
            MapEntity program = JSONObject.parseObject(ja.get(i).toString(), MapEntity.class);
            String name = program.get("name").toString();
            String[] sp = name.split("\\.");
            if (sp.length >= 4) {
                if ((sp[0] + sp[1]).equals("devcommand")) {
                    if (sp[2].equals("sn")) {
                        listMq.add(sp[3]);
                    } else {
                        listMq.add(sp[2]);
                    }
                }
                if (sp[1].equals("hsjdsylocker")) {
                    listMq.add(sp[2]);
                }
            }
        }
        System.out.println("MACsn数量: " + listMq.size());
        return listMq;
    }

    // @Scheduled(cron = "0 */10 * * * ?")
    @Scheduled(initialDelay = 5 * 000, fixedRate = 600 * 1000L)
    public void checkOnline() throws Exception {

        if (StringUtils.isBlank(httpPath)) {
            if (count == 0) {
                logger.debug("RefeshDevDataServicel轮询结束停止");
                count++;
            }
            return;
        }
        List<String> listMq = updateMqOnline();
        System.out.println(listMq);
        List<String> onlineList = new ArrayList<String>();
        List<String> notOnlineList = new ArrayList<String>();
        List<MapEntity> deviceOnlines = refeshDevDataDao.getDeviceOnline();

        for (MapEntity mapEntity : deviceOnlines) {
            String macsn = mapEntity.get("macsn").toString();
            String online = mapEntity.get("online").toString();
            if (listMq.contains(macsn)) {
                mapEntity.put("mqOnline", "1");
            } else {
                mapEntity.put("mqOnline", "0");
            }
        } 
        // ---------------------
        // System.out.println(deviceOnlines);
        for (MapEntity mapEntity : deviceOnlines) {

            String online = mapEntity.get("online").toString();
            String mqOnline = mapEntity.get("mqOnline").toString();
            String id = mapEntity.get("id").toString();
            if (!online.equals(mqOnline)) {
                if (mqOnline.equals("1")) {
                    onlineList.add(id);
                } else {
                    notOnlineList.add(id);
                }
            }
        } // -----------
        System.out.println("onlineList" + onlineList);
        System.out.println("notOnlineList" + notOnlineList);
        if (onlineList != null && onlineList.size() > 0) {
            refeshDevDataDao.onlineList(onlineList);
        }
        if (notOnlineList != null && notOnlineList.size() > 0) {
            refeshDevDataDao.notOnlineList(notOnlineList);
        }
    }

}
