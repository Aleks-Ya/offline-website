package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.JobParams;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.concurrent.Future;

/**
 * @author Aleksey Yablokov
 */
public class JobImpl implements Job {
    private final SiteUrl url;
    private final Downloader downloader;
    private final Storage storage;

    public JobImpl(JobParams params) {
        url = params.getSiteUrl();
        downloader = params.getFactory().getDownloader();
        storage = params.getFactory().getStorage();
    }

    @Override
    @SneakyThrows
    public void process() {
        NewResource.NewResourceId newResourceId = storage.createNewResource(url);
        DownloadingResource.Id downloadingResourceId = storage.createDownloadingResource(newResourceId);
        Future<DownloadedResource.Id> future = downloader.download(downloadingResourceId);
        future.get();
    }
}
