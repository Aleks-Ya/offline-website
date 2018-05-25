package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class ParallelDownloadJob extends SequenceDownloadJob {
    private final ThreadPool threadPool;
    private final List<Future> futures = new ArrayList<>();

    public ParallelDownloadJob(DownloadJobParams params) {
        super(params);
        threadPool = params.getThreadPool();
    }

    @Override
    @SneakyThrows
    public void process() {
        log.info("{} started", getClass().getSimpleName());
        log.info("Root page: " + rootUrl);
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
                if (!futures.isEmpty()) {
                    Future future = futures.get(0);
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("Task failed", e);
                    } finally {
                        futures.remove(future);
                    }
                } else {
                    break;
                }
            }
        }

        log.info("{} finished. Run {} tasks. Completed {} tasks.",
                getClass().getSimpleName(), taskRun, threadPool.getCompletedTaskCount());
    }

    private void submitTask(ResourceId<HeadingRes> hingResId) {
        DownloadTaskParams params = new DownloadTaskParamsImpl(rootUrl, hingResId, downloader, storage,
                headRetriever, parsers, headingFilters, headedFilters);
        futures.add(threadPool.submit(new DownloadTask(params)));
        taskRun++;
    }
}