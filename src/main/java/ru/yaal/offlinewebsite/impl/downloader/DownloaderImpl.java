package ru.yaal.offlinewebsite.impl.downloader;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class DownloaderImpl implements Downloader {
    private final Storage storage;
    private final Network network;

    public DownloaderImpl(@NonNull DownloaderParams params) {
        storage = params.getStorage();
        network = params.getNetwork();
    }

    @Override
    @SneakyThrows
    public ResourceId<DownloadedRes> download(ResourceId<DownloadingRes> didResId) {
        DownloadingRes res = storage.getResource(didResId);
        SiteUrl url = res.getUrl();
        InputStream is = network.openUrl(url);
        OutputStream os = res.getOutputStream();
        log.debug("Downloading: " + res.getId());
        IOUtils.copy(is, os);
        log.debug("Downloaded: " + res.getId());
        return storage.createDownloadedResource(res.getId());
    }
}
