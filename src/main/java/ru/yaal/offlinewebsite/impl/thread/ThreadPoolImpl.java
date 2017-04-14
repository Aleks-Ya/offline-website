package ru.yaal.offlinewebsite.impl.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.concurrent.*;

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
}
