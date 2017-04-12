package ru.yaal.offlinewebsite.impl.params;

import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

/**
 * @author Aleksey Yablokov
 */
public class DownloaderParamsImpl implements DownloaderParams {
    private final Storage storage;
    private final Network network;
    private final ThreadPool threadPool;

    public DownloaderParamsImpl(Storage storage, Network network, ThreadPool threadPool) {
        this.storage = storage;
        this.network = network;
        this.threadPool = threadPool;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadPool;
    }
}
