package ru.yaal.offlinewebsite.impl.task;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.TaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.filter.SameDomainFilter;
import ru.yaal.offlinewebsite.impl.filter.SizeFilter;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class TaskImpl implements Task {
    private final SiteUrl url;
    private final Downloader downloader;
    private final Storage storage;
    private final boolean onlySameDomain;
    private final Filter<NewRes> onlySameDomainFilter;
    private final Filter<HeadedRes> sizeFilter;
    private final HeadRequest headRequest;
    private final long maxSize;
    private final Parser parser;

    public TaskImpl(TaskParams params) {
        url = params.getSiteUrl();
        downloader = params.getDownloader();
        storage = params.getStorage();
        onlySameDomain = params.isOnlySameDomain();
        maxSize = params.getMaxSize();
        onlySameDomainFilter = new SameDomainFilter(params.getRootUrl());
        sizeFilter = new SizeFilter(maxSize);
        headRequest = params.getHeadRequest();
        parser = params.getParser();
    }

    @Override
    public Void call() throws Exception {
        NewRes.Id newResId = storage.createNewResource(url);

        NewRes<NewRes.Id> newRes = storage.getResource(newResId);
        if (onlySameDomain) {
            FilterDecision decision = onlySameDomainFilter.filter(newRes);
            if (!decision.isAccepted()) {
                log.debug("Reject resource: " + decision.getMessage());
                storage.createRejectedRes(newResId);
                return null;
            }
        }

        HeadingRes.Id hingResId = storage.createHeadingResource(newResId);
        HeadedRes.Id hedResId = headRequest.requestHead(hingResId);
        HeadedRes<HeadedRes.Id> hedRes = storage.getResource(hedResId);
        if (maxSize > 0) {
            FilterDecision decision = sizeFilter.filter(hedRes);
            if (!decision.isAccepted()) {
                log.debug("Skip resource by $s (maxSize=%d, resource size=%d: $s",
                        sizeFilter.getClass().getSimpleName(), maxSize, hedRes.getHttpInfo().getContentLength(), newResId);
                storage.createRejectedRes(newResId);
                return null;
            }
        }

        DownloadingRes.Id dingResId = storage.createDownloadingResource(hedResId);
        DownloadedRes.Id dedResId = downloader.download(dingResId);
        ParsingRes.Id pingResId = storage.createParsingRes(dedResId);
        parser.parse(pingResId);
        return null;
    }
}
