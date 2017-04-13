package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface TaskParams extends Params {
    SiteUrl getRootUrl();

    SiteUrl getSiteUrl();

    Downloader getDownloader();

    Storage getStorage();

    boolean isOnlySameDomain();

    HeadRequest getHeadRequest();

    long getMaxSize();
    Parser getParser();
}
