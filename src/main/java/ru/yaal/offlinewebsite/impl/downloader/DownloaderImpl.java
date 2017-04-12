package ru.yaal.offlinewebsite.impl.downloader;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public class DownloaderImpl implements Downloader {
    private final Storage storage;

    public DownloaderImpl(DownloaderParams params) {
        storage = params.getFactory().getStorage();
    }

    @Override
    public Future<DownloadedResource.Id> download(DownloadingResource.Id id) {
        return null;
    }
}
