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
public class SequencePackageJob implements Job {
    private final Storage storage;
    private final List<Packager> packagers;

    public SequencePackageJob(PackageJobParams params) {
        storage = params.getStorage();
        packagers = params.getPackagers();
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

        tasks.forEach(packageTask -> {
            try {
                packageTask.call();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        });

        log.info("{} finished", getClass().getSimpleName());

        log.info("Statistics: " + storage.getStatistics());
    }
}