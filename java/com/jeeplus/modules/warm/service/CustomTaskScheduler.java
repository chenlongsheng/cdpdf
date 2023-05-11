package com.jeeplus.modules.warm.service;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;
 
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

import com.drew.lang.annotations.Nullable;
import com.jeeplus.modules.warm.dao.PdfOrderDao;
 


/**
 * 继承 ThreadPoolTaskScheduler
 * <p>Title: CustomTaskScheduler</p>  
 * <p>Description: TODO(类注释)</p>  
 * @author hfuquan
 * @date 2020年9月12日  上午9:24:52
 * @since JDK1.8
 */
@Service
public class CustomTaskScheduler  {
    @Autowired
    PdfOrderDao pdfOrderDao;

    public void run() {

    System.out.println(pdfOrderDao.alarmCountDetail("5555"));    
        
        
    }


   
    
}
