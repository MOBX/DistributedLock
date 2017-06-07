/*
 * Copyright 2015-2020 uuzu.com All right reserved.
 */
package com.mob.distributed.support;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.lamfire.utils.StringUtils;

/**
 * 线程相关工具类
 * 
 * @author zxc Oct 8, 2015 6:18:22 PM
 */
public class ThreadUtil {

    /** # */
    private final static String             POUND      = "#";
    /** - */
    private final static String             MINUS_SIGN = "-";

    private static ScheduledExecutorService service    = Executors.newScheduledThreadPool(1);

    private static ExecutorService          pool       = Executors.newFixedThreadPool(10);

    public static void submitTask(Runnable runnable) {
        pool.submit(runnable);
    }

    /**
     * 重复开启 threadNum 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     * @param threadNum 重复开启的线程个数
     * @param sleepTime 启动完所有线程后，休息 ms
     */
    public static void startThread(Runnable runnable, String threadName, int threadNum, long sleepTime) {
        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(runnable, POUND + StringUtils.defaultIfEmpty(threadName, "Thread") + MINUS_SIGN + i);
            thread.start();
        }
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 重复开启 threadNum 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     * @param threadNum 重复开启的线程个数
     * @param sleepTime 启动完所有线程后，休息 ms
     */
    public static void startThread(Runnable runnable, int threadNum, long sleepTime) {
        ThreadUtil.startThread(runnable, "Thread", threadNum, sleepTime);
    }

    /**
     * 开启 1 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     */
    public static void startThread(Runnable runnable) {
        startThread(runnable, 1, 0);
    }

    /**
     * 开启 1 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     */
    public static void startThread(Runnable runnable, String threadName) {
        startThread(runnable, StringUtils.trimToEmpty(threadName), 1, 0);
    }

    /**
     * 重复开启 threadNum 个线程来执行 runnable
     * 
     * @param runnable 可执行任务
     * @param sleepTime 重复开启的线程个数
     */
    public static void startThread(Runnable runnable, long sleepTime) {
        startThread(runnable, 1, sleepTime);
    }

    /**
     * 定时执行一个任务
     * 
     * @param task 实现TimerTask接口的任务实例
     * @param delay 调用方法后要延时的毫秒数
     * @param period 执行间隔
     * @throws Exception
     */
    public static void scheduleAtFixedRateDelayTimeMillisDelay(TimerTask task, long delay, long period) throws Exception {
        if (task == null) throw new Exception("task 为空");
        service.scheduleAtFixedRate(task, delay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Sleep thread without exception.
     * 
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
