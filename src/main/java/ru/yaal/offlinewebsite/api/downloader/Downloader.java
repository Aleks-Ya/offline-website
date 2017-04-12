package ru.yaal.offlinewebsite.api.downloader;

import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;

import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public interface Downloader {
    Future<DownloadedResource.Id> download(DownloadingResource.Id id);
}
