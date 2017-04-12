package ru.yaal.offlinewebsite.api.ioc;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface Factory {
    Downloader getDownloader();
    Storage getStorage();
}
