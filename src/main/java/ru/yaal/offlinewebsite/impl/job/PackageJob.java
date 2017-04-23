package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.PackageTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.ResIdImpl;
import ru.yaal.offlinewebsite.impl.task.PackageTask;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class PackageJob implements Job {
    private final Storage storage;
    private final List<Packager> packagers;
    private final ThreadPool threadPool;

    public PackageJob(PackageJobParams params) {
        storage = params.getStorage();
        packagers = params.getPackagers();
        threadPool = params.getThreadPool();
    }

    @Override
    @SneakyThrows
    public void process() {
        log.info("{} started", getClass().getSimpleName());
        List<ResourceId<ParsedRes>> parsedResIds = storage.getParsedResourceIds();
        log.info("Resources for packaging: " + parsedResIds.size());

        List<PackageTask> tasks = parsedResIds.stream()
                .map(storage::createPackagingRes)
                .map(packagingResId -> new PackageTaskParamsImpl(
                        storage, packagers, new ResIdImpl<>(packagingResId.getId())))
                .map(PackageTask::new)
                .collect(Collectors.toList());

        List<Future<ResourceId<PackagedRes>>> futures = tasks.stream()
                .map(threadPool::submit)
                .collect(Collectors.toList());

        log.info("Submitted packaging tasks: " + futures.size());

        for (Future<ResourceId<PackagedRes>> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.warn("Task interrupted", e);
            } catch (ExecutionException e) {
                log.error("Task failed", e);
            }
        }

        log.info("{} finished", getClass().getSimpleName());

        log.info("Statistics: " + storage.getStatistics());
    }
}