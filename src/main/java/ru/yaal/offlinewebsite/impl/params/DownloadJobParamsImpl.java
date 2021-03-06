package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@Getter
public class DownloadJobParamsImpl implements DownloadJobParams {
    private final RootResUrl rootResUrl;
    private final Downloader downloader;
    private final Storage storage;
    private final ThreadPool threadPool;
    private final HeadRetriever headRetriever;
    private final List<Parser> parsers;
    private final List<HeadingResFilter> headingFilters;
    private final List<HeadedResFilter> headedFilters;
}
