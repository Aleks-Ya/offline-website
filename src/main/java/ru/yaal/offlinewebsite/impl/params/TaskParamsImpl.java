package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.TaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
@Getter
@RequiredArgsConstructor
public class TaskParamsImpl implements TaskParams {
    private final SiteUrl rootUrl;
    private final SiteUrl siteUrl;
    private final Downloader downloader;
    private final Storage storage;
    private final boolean onlySameDomain;
    private final HeadRequest headRequest;
    private final long maxSize;
    private final Parser parser;
}
