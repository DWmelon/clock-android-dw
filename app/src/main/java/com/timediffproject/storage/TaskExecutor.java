package com.timediffproject.storage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by melon on 2017/1/4.
 */

public class TaskExecutor {

    private static TaskExecutor mInstance;

    private ExecutorService mExecutorPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 1);

    private TaskExecutor() {

    }

    public synchronized static TaskExecutor getInstance() {
        if (mInstance == null) {
            mInstance = new TaskExecutor();
        }
        return mInstance;
    }

    public void post(Runnable runnable) {
        mExecutorPool.execute(runnable);
    }

}
