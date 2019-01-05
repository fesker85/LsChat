package cc.lzsou.lschat.core.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolHelper {
    public static ExecutorService catchPool = null;
    public static ExecutorService singlePool = null;

    public static void insertTaskToCatchPool(Runnable command) {
        if (catchPool == null) {
            catchPool = Executors.newCachedThreadPool();
        }
        catchPool.execute(command);
    }

    public static void insertTaskToSinglePool(Runnable command) {
        if (singlePool == null) {
            singlePool = Executors.newSingleThreadExecutor();
        }
        singlePool.execute(command);
    }
    /**
     * 关闭所有正在执行的线程
     *
     * @version V1.0
     * @return void
     */
    public static void closeAllThreadPool() {
        if (catchPool != null) {
            catchPool.shutdownNow();
        }
        if (singlePool != null) {
            singlePool.shutdownNow();
        }
    }
}
