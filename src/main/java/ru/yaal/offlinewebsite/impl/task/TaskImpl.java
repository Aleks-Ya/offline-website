package ru.yaal.offlinewebsite.impl.task;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
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
    private final HeadingRes.Id hingResId;
    private final Downloader downloader;
    private final Storage storage;
    private final boolean onlySameDomain;
    private final Filter<HeadingRes> onlySameDomainFilter;
    private final Filter<HeadedRes> sizeFilter;
    private final HeadRequest headRequest;
    private final long maxSize;
    private final Parser parser;

    public TaskImpl(TaskParams params) {
        hingResId = params.getHingResId();
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
        HeadingRes<HeadingRes.Id> hingRes = storage.getResource(hingResId);
        if (onlySameDomain) {
            FilterDecision decision = onlySameDomainFilter.filter(hingRes);
            if (!decision.isAccepted()) {
                log.debug("Reject resource: " + decision.getMessage());
                storage.createRejectedRes(hingResId);
                return null;
            }
        }

        HeadedRes.Id hedResId = headRequest.requestHead(hingResId);
        HeadedRes<HeadedRes.Id> hedRes = storage.getResource(hedResId);
        if (maxSize > 0) {
            FilterDecision decision = sizeFilter.filter(hedRes);
            if (!decision.isAccepted()) {
                log.debug("Skip resource by $s (maxSize=%d, resource size=%d: $s",
                        sizeFilter.getClass().getSimpleName(), maxSize,
                        hedRes.getHttpInfo().getContentLength(), hingResId);
                storage.createRejectedRes(hingResId);
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
