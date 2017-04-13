package ru.yaal.offlinewebsite.impl.job;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.JobParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public class JobImpl implements Job {
    private final SiteUrl url;
    private final Downloader downloader;
    private final Storage storage;

    public JobImpl(JobParams params) {
        url = params.getSiteUrl();
        downloader = params.getDownloader();
        storage = params.getStorage();
    }

    @Override
    @SneakyThrows
    public void process() {
    }
}
