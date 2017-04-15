package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
@Getter
@RequiredArgsConstructor
public class DownloadTaskParamsImpl implements DownloadTaskParams {
    private final SiteUrl rootUrl;
    private final ResourceId<HeadingRes> hingResId;
    private final Downloader downloader;
    private final Storage storage;
    private final boolean onlySameDomain;
    private final HeadRequest headRequest;
    private final long maxSize;
    private final Parser parser;
}
