package ru.yaal.offlinewebsite.impl.thread;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class ThreadPoolImpl implements ThreadPool {
    private final int size;
    private ExecutorService pool;

    public ThreadPoolImpl(ThreadPoolParams params) {
        this.size = params.getPoolSize();
        log.debug("ThreadPool created with params: " + params);
    }

    @Override
    public synchronized <V> Future<V> submit(Callable<V> callable) {
        log.debug("Submit callable");
        if (pool == null) {
            pool = Executors.newFixedThreadPool(size);
        }
        return pool.submit(callable);
    }

    @Override
    public void shutdown() {
        pool.shutdown();
    }
}
