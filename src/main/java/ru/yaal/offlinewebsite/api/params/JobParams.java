package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

/**
 * @author Aleksey Yablokov
 */
public interface JobParams extends Params {
    SiteUrl getRootSiteUrl();

    Downloader getDownloader();

    Storage getStorage();

    ThreadPool getThreadPool();

    HeadRequest getHeadRequest();

    Parser getParser();
}
