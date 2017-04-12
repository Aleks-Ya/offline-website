package ru.yaal.offlinewebsite.impl.downloader;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class DownloaderImpl implements Downloader {
    private final Storage storage;
    private final Network network;
    private final ThreadPool threadPool;

    public DownloaderImpl(@NonNull DownloaderParams params) {
        storage = params.getStorage();
        network = params.getNetwork();
        threadPool = params.getThreadPool();
    }

    @Override
    @SneakyThrows
    public Future<BytesDownloadedResource.Id> download(DownloadingResourceImpl.Id didResId) {
        DownloadingResource<DownloadingResource.Id> res = storage.getResource(didResId);
        SiteUrl url = res.getUrl();
        InputStream is = network.openUrl(url);
        OutputStream os = res.getOutputStream();
        Callable<BytesDownloadedResource.Id> callable = () -> {
            log.info("Downloading: " + res.getId());
            IOUtils.copy(is, os);
            log.info("Downloaded: " + res.getId());
            return storage.createDownloadedResource(res.getId());
        };
        log.debug("Future is created for downloading: " + didResId);
        return threadPool.submit(callable);
    }
}
