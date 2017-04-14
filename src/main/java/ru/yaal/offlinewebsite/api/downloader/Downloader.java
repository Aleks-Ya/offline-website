package ru.yaal.offlinewebsite.api.downloader;

import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface Downloader {
    ResourceId<DownloadedRes> download(ResourceId<DownloadingRes> id);
}
