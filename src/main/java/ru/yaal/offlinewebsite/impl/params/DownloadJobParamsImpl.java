package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.RootSiteUrl;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class DownloadJobParamsImpl implements DownloadJobParams {
    private final RootSiteUrl rootSiteUrl;
    private final Downloader downloader;
    private final Storage storage;
    private final ThreadPool threadPool;
    private final HeadRetriever headRetriever;
    private final List<Parser> parsers;
}
