package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.PackageTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.task.PackageTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class PackageJob implements Job {
    private final Storage storage;
    private final Packager<TagNode> packager;
    private final ThreadPool threadPool;

    public PackageJob(PackageJobParams<TagNode> params) {
        storage = params.getStorage();
        packager = params.getPackager();
        threadPool = params.getThreadPool();
    }

    @Override
    @SneakyThrows
    public void process() {
        log.debug("Job started");
        List<ResourceId<PackagingRes<TagNode>>> packagingResIds = storage.getPackagingResourceIds();

        List<Future<Void>> futures = new ArrayList<>();
        for (ResourceId<PackagingRes<TagNode>> packagingResId : packagingResIds) {
            PackageTaskParams<TagNode> taskParams = new PackageTaskParamsImpl<>(storage, packager, packagingResId);
            futures.add(threadPool.submit(new PackageTask(taskParams)));
        }

        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.warn("Task interrupted", e);
            } catch (ExecutionException e) {
                log.error("Task failed", e);
            }
        }

    }
}