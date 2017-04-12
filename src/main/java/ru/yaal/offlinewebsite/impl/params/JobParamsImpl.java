package ru.yaal.offlinewebsite.impl.params;

import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.params.JobParams;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public class JobParamsImpl implements JobParams {
    private final SiteUrl url;
    private final Downloader downloader;
    private final Storage storage;

    public JobParamsImpl(SiteUrl url, Downloader downloader, Storage storage) {
        this.url = url;
        this.downloader = downloader;
        this.storage = storage;
    }

    @Override
    public SiteUrl getSiteUrl() {
        return url;
    }

    @Override
    public Downloader getDownloader() {
        return downloader;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }
}
