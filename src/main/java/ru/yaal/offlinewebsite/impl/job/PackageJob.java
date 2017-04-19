package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.params.PackageTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;
import ru.yaal.offlinewebsite.impl.task.PackageTask;

import java.io.InputStream;
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
    private final Packager<TagNode> httpPackager;
    private final Packager<InputStream> isPackager;
    private final ThreadPool threadPool;
    private final List<Replacer<TagNode>> replacers;

    public PackageJob(PackageJobParams params) {
        storage = params.getStorage();
        httpPackager = params.getHtmlPackager();
        isPackager = params.getInputStreamPackager();
        threadPool = params.getThreadPool();
        replacers = params.getReplacers();
    }

    @Override
    @SneakyThrows
    public void process() {
        log.debug("Job started");
        List<ResourceId<ParsedRes<TagNode>>> parsedResIds = storage.getParsedResourceIds();
        log.debug("Resources for packaging: " + parsedResIds.size());

        List<Future<ResourceId<PackagedRes>>> futures = parsedResIds.stream()
                .map(storage::createPackagingRes)
                .map(packagingResId -> new PackageTaskParamsImpl(
                        storage, httpPackager, isPackager, new ResourceIdImpl<>(packagingResId.getId()), replacers))
                .map(PackageTask::new)
                .map(threadPool::submit)
                .collect(Collectors.toList());

        log.debug("Submitted packaging tasks: " + futures.size());

        for (Future<ResourceId<PackagedRes>> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.warn("Task interrupted", e);
            } catch (ExecutionException e) {
                log.error("Task failed", e);
            }
        }
        log.debug("Job finished");
    }
}