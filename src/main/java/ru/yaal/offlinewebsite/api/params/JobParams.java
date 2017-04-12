package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface JobParams extends Params {
    SiteUrl getSiteUrl();
    Downloader getDownloader();
    Storage getStorage();
}
