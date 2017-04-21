package ru.yaal.offlinewebsite.impl.ioc;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.ioc.Factory;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;

/**
 * @author Aleksey Yablokov
 */
public class FactoryImpl implements Factory {
    private static Factory INSTANCE = new FactoryImpl();
    private final Downloader downloader = new DownloaderImpl(null);
    private final Storage storage = new SyncInMemoryStorageImpl(new StorageParamsImpl());

    private FactoryImpl() {
    }

    @Override
    public Downloader getDownloader() {
        return downloader;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    public static Factory getInstance() {
        return INSTANCE;
    }

    public static void setInstance(Factory factory) {

    }
}
