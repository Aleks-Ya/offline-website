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
import ru.yaal.offlinewebsite.impl.params.DownloadTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.task.DownloadTask;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class SequenceDownloadJob implements Job {
    final RootResUrl rootUrl;
    final Downloader downloader;
    final Storage storage;
    final HeadRetriever headRetriever;
    final List<Parser> parsers;
    final List<HeadingResFilter> headingFilters;
    final List<HeadedResFilter> headedFilters;
    long taskRun = 0;

    public SequenceDownloadJob(DownloadJobParams params) {
        rootUrl = params.getRootResUrl();
        downloader = params.getDownloader();
        storage = params.getStorage();
        headRetriever = params.getHeadRetriever();
        headingFilters = params.getHeadingFilters();
        headedFilters = params.getHeadedFilters();
        parsers = params.getParsers();
    }

    @Override
    @SneakyThrows
    public void process() {
        log.info("{} started", getClass().getSimpleName());
        log.info("Root page: " + rootUrl);
        ResourceId<NewRes> rootNewResId = storage.createNewResource(rootUrl);
        ResourceId<HeadingRes> rootHingResId = storage.createHeadingResource(rootNewResId);
        processResource(rootHingResId);

        for (; ; ) {
            List<ResourceId<NewRes>> newResIds = storage.getNewResourceIds();
            if (newResIds.isEmpty()) {
                log.info("{} finished. Run {} tasks", getClass().getSimpleName(), taskRun);
                return;
            }
            newResIds.stream()
                    .map(storage::createHeadingResource)
                    .forEach(this::processResource);
        }
    }

    @SneakyThrows
    private void processResource(ResourceId<HeadingRes> hingResId) {
        DownloadTaskParams params = new DownloadTaskParamsImpl(rootUrl, hingResId, downloader, storage,
                headRetriever, parsers, headingFilters, headedFilters);
        DownloadTask task = new DownloadTask(params);
        task.call();
        taskRun++;
    }
}
