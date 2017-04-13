package ru.yaal.offlinewebsite.api.downloader;

import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;

/**
 * @author Aleksey Yablokov
 */
public interface Downloader {
    DownloadedRes.Id download(DownloadingRes.Id id);
}
