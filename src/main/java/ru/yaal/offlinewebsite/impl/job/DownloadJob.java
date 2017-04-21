package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.DownloadTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.task.DownloadTask;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class DownloadJob implements Job {
    private final RootPageUrl rootUrl;
    private final Downloader downloader;
    private final Storage storage;
    private final ThreadPool threadPool;
    private final HeadRetriever headRetriever;
    private final List<Parser> parsers;
    private long taskRun = 0;

    public DownloadJob(DownloadJobParams params) {
        rootUrl = params.getRootPageUrl();
        downloader = params.getDownloader();
        storage = params.getStorage();
        threadPool = params.getThreadPool();
        headRetriever = params.getHeadRetriever();
        parsers = params.getParsers();
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
                if (threadPool.getCompletedTaskCount() == taskRun) {
                    break;
                }
            }
        }

        log.debug("Job finished");
    }

    private void submitTask(ResourceId<HeadingRes> hingResId) {
        DownloadTaskParams params = new DownloadTaskParamsImpl(rootUrl, hingResId, downloader, storage,
                true, headRetriever, 1_000_000, parsers);
        threadPool.submit(new DownloadTask(params));
        taskRun++;
    }
}
