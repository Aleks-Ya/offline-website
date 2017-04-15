package ru.yaal.offlinewebsite.impl.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class ThreadPoolImpl implements ThreadPool {
    private final int size;
    private ThreadPoolExecutor pool;

    public ThreadPoolImpl(ThreadPoolParams params) {
        this.size = params.getPoolSize();
        log.debug("ThreadPool created with params: " + params);
    }

    @Override
    public synchronized <V> Future<V> submit(Callable<V> callable) {
        log.debug("Submit callable");
        if (pool == null) {
            pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(size);
        }
        return pool.submit(callable);
    }

    @Override
    @SneakyThrows
    public void shutdown() {
        pool.shutdown();
        boolean successful = pool.awaitTermination(5, TimeUnit.SECONDS);
        if (!successful) {
            throw new RuntimeException("ThreadPool terminated");
        }
    }

    @Override
    public boolean isShutdown() {
        return pool.isShutdown();
    }

    @Override
    public long getCompletedTaskCount() {
        return pool.getCompletedTaskCount();
    }
}
