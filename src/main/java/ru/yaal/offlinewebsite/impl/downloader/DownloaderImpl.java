package ru.yaal.offlinewebsite.impl.downloader;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public class DownloaderImpl implements Downloader {
    private final Storage storage;
    private final Network network;
    private final ThreadPool threadPool;

    public DownloaderImpl(DownloaderParams params) {
        storage = params.getStorage();
        network = params.getNetwork();
        threadPool = params.getThreadPool();
    }

    @Override
    @SneakyThrows
    public Future<BytesDownloadedResource.Id> download(DownloadingResourceImpl.Id id) {
        DownloadingResource<DownloadingResourceImpl.Id> res = storage.getResource(id);
        SiteUrl url = res.getUrl();
        InputStream is = network.openUrl(url);
        OutputStream os = res.getOutputStream();
        Callable<BytesDownloadedResource.Id> callable = () -> {
            IOUtils.copy(is, os);
            return storage.createDownloadedResource(res.getId());
        };
        return threadPool.execute(callable);
    }
}
