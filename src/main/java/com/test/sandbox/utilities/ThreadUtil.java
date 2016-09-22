package com.test.sandbox.utilities;

import java.util.concurrent.*;

/**
 * Utility helper class for handling Thread based operations
 */
public class ThreadUtil {
    public static Future<?> executeCachedThread(Runnable runnable) {
        ExecutorService executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.MINUTES, (BlockingQueue) new SynchronousQueue<>());
        return executor.submit(runnable);
    }
}
