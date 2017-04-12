package ru.yaal.offlinewebsite;

import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.JobParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.job.JobImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.JobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.storage.SynchronizedInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.NetworkImpl;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class OfflineWebsite {
    public static void download(URL url) {
        SiteUrl siteUrl = new SiteUrlImpl(url.toString());
        Storage storage = new SynchronizedInMemoryStorageImpl();
        Network network = new NetworkImpl();
        int poolSize = 10;
        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(poolSize);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);
        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network, threadPool);
        Downloader downloader = new DownloaderImpl(downloaderParams);
        JobParams jobParams = new JobParamsImpl(siteUrl, downloader, storage);
        Job job = new JobImpl(jobParams);
        job.process();
    }
}
