package ru.yaal.offlinewebsite.impl.thread;

import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public class ThreadPoolImpl implements ThreadPool {
    private final int size;
    private ExecutorService pool;

    public ThreadPoolImpl(ThreadPoolParams params) {
        this.size = params.getPoolSize();
    }

    @Override
    public <V> Future<V> execute(Callable<V> callable) {
        if (pool == null) {
            pool = Executors.newFixedThreadPool(size);
        }
        return pool.submit(callable);
    }
}
