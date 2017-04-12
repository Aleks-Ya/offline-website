package ru.yaal.offlinewebsite.api.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public interface ThreadPool {
    <V> Future<V> execute(Callable<V> callable);
}
