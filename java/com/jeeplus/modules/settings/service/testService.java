/**
 * 
 */
package com.jeeplus.modules.settings.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;

import com.jeeplus.modules.settings.dao.RefeshDevDataDao;

/**
 * @author admin
 *
 */
public class testService {
    @Autowired
    RefeshDevDataDao refeshDevDataDao;

    public void timer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                refeshDevDataDao.getDeviceOnline();
            }
        }, 2000);// 设定指定的时间time,此处为2000毫秒
    }

}
