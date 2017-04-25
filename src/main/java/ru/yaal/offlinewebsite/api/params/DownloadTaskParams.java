package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
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
    RootResUrl getRootResUrl();

    ResourceId<HeadingRes> getHingResId();

    Downloader getDownloader();

    Storage getStorage();

    HeadRetriever getHeadRetriever();

    List<Parser> getParsers();

    List<HeadingResFilter> getHeadingFilters();

    List<HeadedResFilter> getHeadedFilters();
}
