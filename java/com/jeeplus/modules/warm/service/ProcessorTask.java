/**
 * 
 */
package com.jeeplus.modules.warm.service;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;

/**
 * @author admin
 *
 */
@Service
@Lazy(false)
public class ProcessorTask {
    // 给每个方法定义一个常量名字
    public static final String CANCEL_TASK_METHOD_NAME = "doFixedRate";
    public static final String CANCEL_TASK_METHOD_NAME_2 = "doFixedDelay";
    private static final int MAX_COUNTS = 5;
    private int counts = 10;

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

//    @Scheduled(cron = "30 10 1 ? 10 SUN")
    public void fixedRateJob() {

        ScheduledFuture<?> future = scheduledTasks.get(this);
        future.cancel(false);
        System.out.println("Something to be done every 2 secs");
    }

    @Bean
    public TaskScheduler poolScheduler() {
        return new CustomTaskScheduler();
    }

    class CustomTaskScheduler extends ThreadPoolTaskScheduler {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }

        @Override
        public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
            ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);

            ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
            scheduledTasks.put(runnable.getTarget(), future);

            return future;
        }

    }
}
