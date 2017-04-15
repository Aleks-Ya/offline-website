package ru.yaal.offlinewebsite.impl.job;

import org.junit.Test;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.params.*;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HeadRequestImpl;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.params.*;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

/**
 * @author Aleksey Yablokov
 */
public class DownloadAndParseImplTest {
    @Test
    public void process() {
        String rootUrl = "http://ya.ru";
        String html = "<html></html>";
        int responseCode = 200;
        int contentLength = 100_000;
        int lastModified = 2000000;

        SiteUrl rootSiteUrl = new SiteUrlImpl(rootUrl);
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        Storage storage = new SyncInMemoryStorageImpl(storageParams);
        HttpInfo httpInfo = new HttpInfoImpl(responseCode, contentLength, lastModified);
        BytesNetwork network = new BytesNetwork();
        network.putBytes(rootSiteUrl, html);
        network.putHttpInfo(rootSiteUrl, httpInfo);
        DownloaderParams params = new DownloaderParamsImpl(storage, network);
        Downloader downloader = new DownloaderImpl(params);
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        HeadRequest headRequest = new HeadRequestImpl(headRequestParams);
        ParserParams parserParams = new ParserParamsImpl(storage, rootSiteUrl);
        Parser parser = new ParserImpl(parserParams);
        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(3);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);

        JobParams jobParams = new JobParamsImpl(rootSiteUrl, downloader, storage, threadPool, headRequest, parser);
        Job job = new DownloadAndParseImpl(jobParams);
        job.process();
    }

}