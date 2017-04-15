package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.DownloadTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.task.DownloadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class DownloadJob implements Job {
    private final SiteUrl rootUrl;
    private final Downloader downloader;
    private final Storage storage;
    private final ThreadPool threadPool;
    private final HeadRequest headRequest;
    private final Parser parser;
    private final List<Future> futures = new ArrayList<>();

    public DownloadJob(DownloadJobParams params) {
        rootUrl = params.getRootSiteUrl();
        downloader = params.getDownloader();
        storage = params.getStorage();
        threadPool = params.getThreadPool();
        headRequest = params.getHeadRequest();
        parser = params.getParser();
    }

    @Override
    @SneakyThrows
    public void process() {
        log.debug("Job started");
        ResourceId<NewRes> rootNewResId = storage.createNewResource(rootUrl);
        ResourceId<HeadingRes> rootHingResId = storage.createHeadingResource(rootNewResId);
        submitTask(rootHingResId);

        for (; ; ) {
            List<ResourceId<NewRes>> newResIds = storage.getNewResourceIds();
            if (!newResIds.isEmpty()) {
                newResIds.stream()
                        .map(storage::createHeadingResource)
                        .forEach(this::submitTask);
            } else {
                removeFinishedFutures();
                if (futures.isEmpty()) {
                    break;
                }
            }
        }

        log.debug("Job finished");
    }

    private void removeFinishedFutures() {
        List<Future> doneFutures = futures.stream().filter(f -> f.isDone() || f.isCancelled()).collect(Collectors.toList());
        futures.removeAll(doneFutures);
    }

    private void submitTask(ResourceId<HeadingRes> hingResId) {
        DownloadTaskParams params = new DownloadTaskParamsImpl(rootUrl, hingResId, downloader, storage,
                true, headRequest, 1_000_000, parser);
        futures.add(threadPool.submit(new DownloadTask(params)));
    }
}