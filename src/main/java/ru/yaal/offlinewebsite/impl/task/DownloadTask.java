package ru.yaal.offlinewebsite.impl.task;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.filter.SameDomainFilter;
import ru.yaal.offlinewebsite.impl.filter.SizeFilter;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class DownloadTask implements Task {
    private final ResourceId<HeadingRes> hingResId;
    private final Downloader downloader;
    private final Storage storage;
    private final boolean onlySameDomain;
    private final Filter<HeadingRes> onlySameDomainFilter;
    private final Filter<HeadedRes> sizeFilter;
    private final HeadRequest headRequest;
    private final long maxSize;
    private final List<Parser<Object>> parsers;

    public DownloadTask(DownloadTaskParams params) {
        hingResId = params.getHingResId();
        downloader = params.getDownloader();
        storage = params.getStorage();
        onlySameDomain = params.isOnlySameDomain();
        maxSize = params.getMaxSize();
        onlySameDomainFilter = new SameDomainFilter(params.getRootUrl());
        sizeFilter = new SizeFilter(maxSize);
        headRequest = params.getHeadRequest();
        parsers = params.getParsers();
    }

    @Override
    public Void call() throws Exception {
        HeadingRes hingRes = storage.getResource(hingResId);
        if (onlySameDomain) {
            FilterDecision decision = onlySameDomainFilter.filter(hingRes);
            if (!decision.isAccepted()) {
                log.debug("Reject resource: " + decision.getMessage());
                storage.createRejectedRes(hingResId);
                return null;
            }
        }

        ResourceId<HeadedRes> hedResId = headRequest.requestHead(hingResId);
        HeadedRes hedRes = storage.getResource(hedResId);
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

        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        ResourceId<DownloadedRes> dedResId = downloader.download(dingResId);
        ResourceId<ParsingRes<Object>> parsingResId = storage.createParsingRes(dedResId);
        ParsingRes<Object> parsingRes = storage.getResource(parsingResId);

        parsers.stream()
                .sorted((p1, p2) -> Integer.valueOf(p1.getPriority()).compareTo(p2.getPriority()))
                .filter(parser -> parser.accept(parsingRes.getHttpInfo().getContentType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No parser for: " + parsingResId))
                .parse(parsingResId);

        return null;
    }
}
