package ru.yaal.offlinewebsite;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.*;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HeadRequestImpl;
import ru.yaal.offlinewebsite.impl.job.JobImpl;
import ru.yaal.offlinewebsite.impl.params.*;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.NetworkImpl;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class OfflineWebsite {

    public static void download(URL url) {
        log.info("Start downloading: " + url);
        SiteUrl rootSiteUrl = new SiteUrlImpl(url.toString());
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        Storage storage = new SyncInMemoryStorageImpl(storageParams);
        Network network = new NetworkImpl();
        int poolSize = 10;
        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(poolSize);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);
        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network);
        Downloader downloader = new DownloaderImpl(downloaderParams);
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        HeadRequest headRequest = new HeadRequestImpl(headRequestParams);
        ParserParams parserParams = new ParserParamsImpl(storage, rootSiteUrl);
        Parser parser = new ParserImpl(parserParams);
        JobParams jobParams = new JobParamsImpl(rootSiteUrl, downloader, storage, threadPool, headRequest, parser);
        Job job = new JobImpl(jobParams);
        job.process();
        threadPool.shutdown();
        log.info("Downloading finished: " + url);
    }

    public static void main(String[] args) throws MalformedURLException {
        download(new URL("https://logback.qos.ch/documentation.html"));
    }
}
