package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadTaskParams extends Params {
    RootSiteUrl getRootUrl();

    ResourceId<HeadingRes> getHingResId();

    Downloader getDownloader();

    Storage getStorage();

    boolean isOnlySameDomain();

    HeadRetriever getHeadRetriever();

    long getMaxSize();

    List<Parser> getParsers();
}
