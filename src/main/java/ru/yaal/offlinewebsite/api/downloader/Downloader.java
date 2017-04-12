package ru.yaal.offlinewebsite.api.downloader;

import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;

import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public interface Downloader {
    Future<BytesDownloadedResource.Id> download(DownloadingResourceImpl.Id id);
}
