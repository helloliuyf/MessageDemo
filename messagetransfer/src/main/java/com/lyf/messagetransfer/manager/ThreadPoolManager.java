package com.lyf.messagetransfer.manager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lyf on 2016/12/23 0023.
 * e_mail:helloliuyf@163.com
 */

public class ThreadPoolManager {

    private ThreadPoolExecutor threadPoolExecutor;  // 线程池
    private TimeUnit unit = TimeUnit.DAYS;
    private long keepAliveTime = 2;
    private int maximumPoolSize;
    private int corePoolSize;
    // 拒绝执行机制
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    private BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();


    private static ThreadPoolManager mInstance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance(){
         return mInstance;
    }

    private ThreadPoolManager(){
        // 获取核心线程池的数量
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        // 最大线程池的大小就是核心线程的大小
        maximumPoolSize = corePoolSize;

        // 1:创建线程池
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,  // 核心线程池
                maximumPoolSize, //最大线程池
                keepAliveTime,   //存活时间
                unit,            //时间单位
                workQueue,       // 缓冲队列
                Executors.defaultThreadFactory(), //默认的线程工厂
                handler);    // 拒绝执行机制

    }

    /**
     * 往线程池里面添加任务
     * @param runnable
     */
    public void execute(Runnable runnable){
        if (runnable != null){
            threadPoolExecutor.execute(runnable);
        }
    }

    /**
     * 从线程池里面移除任务
     * @param runnable
     */
    public void remove(Runnable runnable){
        if (runnable != null){
            threadPoolExecutor.remove(runnable);
        }
    }
}
