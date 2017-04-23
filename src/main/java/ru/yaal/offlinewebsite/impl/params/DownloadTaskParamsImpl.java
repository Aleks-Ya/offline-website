package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Getter
@RequiredArgsConstructor
public class DownloadTaskParamsImpl implements DownloadTaskParams {
    private final RootPageUrl rootPageUrl;
    private final ResourceId<HeadingRes> hingResId;
    private final Downloader downloader;
    private final Storage storage;
    private final HeadRetriever headRetriever;
    private final List<Parser> parsers;
    private final List<HeadingResFilter> headingFilters;
    private final List<HeadedResFilter> headedFilters;
}
