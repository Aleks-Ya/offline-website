package ru.yaal.offlinewebsite.impl.task;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
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
import ru.yaal.offlinewebsite.impl.storage.DownloadingExceptionRejectCause;
import ru.yaal.offlinewebsite.impl.storage.FilterRejectCause;
import ru.yaal.offlinewebsite.impl.storage.ParsingExceptionRejectCause;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class DownloadTask implements Task {
    private final ResourceId<HeadingRes> hingResId;
    private final Downloader downloader;
    private final Storage storage;
    private final List<HeadingResFilter> headingFilters;
    private final List<HeadedResFilter> headedFilters;
    private final HeadRetriever headRetriever;
    private final List<Parser> parsers;

    public DownloadTask(DownloadTaskParams params) {
        hingResId = params.getHingResId();
        downloader = params.getDownloader();
        storage = params.getStorage();
        headRetriever = params.getHeadRetriever();
        parsers = params.getParsers();
        parsers.sort((p1, p2) -> Integer.valueOf(p2.getPriority()).compareTo(p1.getPriority()));
        headingFilters = params.getHeadingFilters();
        headedFilters = params.getHeadedFilters();
        log.debug("Parsers: " + parsers);
    }

    @Override
    public Void call() throws Exception {
        HeadingRes hingRes = storage.getResource(hingResId);

        Optional<FilterDecision> notAcceptedHeadingFilter = headingFilters.stream()
                .map(filter -> filter.filter(hingRes))
                .filter(decision -> !decision.isAccepted())
                .findFirst();
        if (notAcceptedHeadingFilter.isPresent()) {
            FilterDecision decision = notAcceptedHeadingFilter.get();
            storage.createRejectedRes(hingResId, new FilterRejectCause(decision));
            return null;
        }

        ResourceId<HeadedRes> hedResId = headRetriever.requestHead(hingResId);
        HeadedRes hedRes = storage.getResource(hedResId);

        Optional<FilterDecision> notAcceptedHeadedFilter = headedFilters.stream()
                .map(filter -> filter.filter(hedRes))
                .filter(decision -> !decision.isAccepted())
                .findFirst();
        if (notAcceptedHeadedFilter.isPresent()) {
            FilterDecision decision = notAcceptedHeadedFilter.get();
            storage.createRejectedRes(hedResId, new FilterRejectCause(decision));
            return null;
        }

        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        ResourceId<DownloadedRes> dedResId;
        try {
            dedResId = downloader.download(dingResId);
        } catch (Exception e) {
            storage.createRejectedRes(dingResId, new DownloadingExceptionRejectCause(e));
            return null;
        }
        ResourceId<ParsingRes> parsingResId = storage.createParsingRes(dedResId);
        ParsingRes parsingRes = storage.getResource(parsingResId);
        String contentType = parsingRes.getHttpInfo().getContentType();

        try {
            parsers.stream()
                    .filter(parser -> parser.accept(contentType))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No parser for: " + parsingRes))
                    .parse(parsingResId);
        } catch (Exception e) {
            storage.createRejectedRes(dingResId, new ParsingExceptionRejectCause(e));
        }
        return null;
    }
}
