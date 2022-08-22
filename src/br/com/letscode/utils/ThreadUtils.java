package br.com.letscode.utils;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class ThreadUtils {
    private static final String INTERRUPTED_EXCEPTION_MESSAGE = "Error while waiting for a thread to finish";

    public static void waitForThreadsToFinish(ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutor.shutdown();
        try {
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(INTERRUPTED_EXCEPTION_MESSAGE, e);
        }
    }
}
