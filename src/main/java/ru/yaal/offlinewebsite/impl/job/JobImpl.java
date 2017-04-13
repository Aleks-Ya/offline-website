package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.JobParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.TaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.TaskParamsImpl;
import ru.yaal.offlinewebsite.impl.task.TaskImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class JobImpl implements Job {
    private final SiteUrl url;
    private final Downloader downloader;
    private final Storage storage;
    private final ThreadPool threadPool;
    private final HeadRequest headRequest;
    private final Parser parser;

    public JobImpl(JobParams params) {
        url = params.getSiteUrl();
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
        List<Future> futures = new ArrayList<>();
        TaskParams params = new TaskParamsImpl(url, url, downloader, storage, true, headRequest,
                1_000_000, parser);
        Task task = new TaskImpl(params);
        futures.add(threadPool.submit(task));
        for (Future future : futures) {
            future.get();
        }
//        List<NewRes.Id> newResourceIds = storage.getNewResourceIds();
        log.debug("Job finished");
    }
}
