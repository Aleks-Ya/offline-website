package ru.yaal.offlinewebsite;

import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.params.PackagerParams;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HeadRequestImpl;
import ru.yaal.offlinewebsite.impl.job.DownloadJob;
import ru.yaal.offlinewebsite.impl.job.PackageJob;
import ru.yaal.offlinewebsite.impl.packager.FolderPackager;
import ru.yaal.offlinewebsite.impl.params.DownloadJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HeadRequestParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PackageJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.HrefUrlExtractor;
import ru.yaal.offlinewebsite.impl.parser.LinkUrlExtractor;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.parser.ScriptUrlExtractor;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.NetworkImpl;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class OfflineWebsite {

    public static void download(URL url, Path outletDir) {
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
        List<UrlExtractor<TagNode>> extractors
                = Arrays.asList(new HrefUrlExtractor(), new LinkUrlExtractor(), new ScriptUrlExtractor());
        ParserParams<TagNode> parserParams = new ParserParamsImpl<>(storage, rootSiteUrl, extractors);
        Parser<TagNode> parser = new ParserImpl(parserParams);

        OfflinePathResolver offlinePathResolver = new OfflinePathResolverImpl();
        PackagerParams packagerParams = new PackagerParamsImpl(outletDir, offlinePathResolver, storage);
        Packager<TagNode> packager = new FolderPackager(packagerParams);

        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(rootSiteUrl, downloader, storage, threadPool, headRequest, parser);
        Job downloadJob = new DownloadJob(downloadJobParams);
        downloadJob.process();

        PackageJobParams<TagNode> packageJobParams = new PackageJobParamsImpl<>(storage, packager, threadPool);
        Job packageJob = new PackageJob(packageJobParams);
        packageJob.process();

        threadPool.shutdown();
        log.info("Downloading finished: " + url);
    }

    public static void main(String[] args) throws IOException {
        Path outletDir = Files.createTempDirectory(OfflineWebsite.class.getSimpleName());
        download(new URL("https://logback.qos.ch/documentation.html"), outletDir);
    }
}
