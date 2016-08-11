package com.myee.tarot.web.apiold.api.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池管理类
 * Created by xiong,An android project Engineer,on 1/7/2016.
 * Data:1/7/2016  下午 06:08
 * Base on clever-m.com(JAVA Service)
 * Describe:
 * Version:1.0
 * Open source
 */
public class ThreadManager {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAXIMUM_POOL_SIZE = (CPU_COUNT * 2 + 1)*2;
    private static ThreadManager INSTANCE;
    private int HOW_MANY_THREADS = MAXIMUM_POOL_SIZE;
    ExecutorService executorService;

    private ThreadManager() {
        executorService = Executors.newFixedThreadPool(HOW_MANY_THREADS);
    }

    public static ThreadManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThreadManager();
        }
        return INSTANCE;
    }

    public void onDestory() {
        executorService.shutdown();
    }

    public void execute(Runnable runnable) {
        if (!executorService.isShutdown()) {
            executorService.execute(runnable);
        } else
            throw new IllegalStateException("Thread pool executor already destroyed");
    }
}
