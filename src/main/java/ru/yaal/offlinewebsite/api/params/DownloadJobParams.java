package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadJobParams extends Params {
    RootSiteUrl getRootSiteUrl();

    Downloader getDownloader();

    Storage getStorage();

    ThreadPool getThreadPool();

    HeadRetriever getHeadRequest();

    List<Parser> getParsers();
}
