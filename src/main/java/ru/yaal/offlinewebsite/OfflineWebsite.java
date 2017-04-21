package ru.yaal.offlinewebsite;

import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.CopyPackagerParams;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.params.UuidLinkPackagerParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HeadRetrieverImpl;
import ru.yaal.offlinewebsite.impl.job.DownloadJob;
import ru.yaal.offlinewebsite.impl.job.PackageJob;
import ru.yaal.offlinewebsite.impl.packager.CopyPackager;
import ru.yaal.offlinewebsite.impl.packager.UuidLinkPackager;
import ru.yaal.offlinewebsite.impl.params.CopyPackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloadJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HeadRequestParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PackageJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.params.UuidLinkPackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.HtmlParser;
import ru.yaal.offlinewebsite.impl.parser.TagAttributeExtractor;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.NetworkImpl;
import ru.yaal.offlinewebsite.impl.thread.ThreadPoolImpl;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class OfflineWebsite {

    public static void download(URL url, Path outletDir) {
        log.info("Start downloading: " + url);
        RootPageUrl rootPageUrl = new PageUrlImpl(url.toString());
        StorageParams storageParams = new StorageParamsImpl();
        Storage storage = new SyncInMemoryStorageImpl(storageParams);
        Network network = new NetworkImpl();
        int poolSize = 10;
        ThreadPoolParams threadPoolParams = new ThreadPoolParamsImpl(poolSize);
        ThreadPool threadPool = new ThreadPoolImpl(threadPoolParams);
        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network);
        Downloader downloader = new DownloaderImpl(downloaderParams);
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        HeadRetriever headRetriever = new HeadRetrieverImpl(headRequestParams);
        List<UrlExtractor<TagNode>> extractors = Arrays.asList(
                new TagAttributeExtractor(new TagAttributeExtractor.Params("a", "href")),
                new TagAttributeExtractor(new TagAttributeExtractor.Params("link", "href")),
                new TagAttributeExtractor(new TagAttributeExtractor.Params("script", "href")));
        ParserParams<TagNode> htmlParserParams = new ParserParamsImpl<>(storage, rootPageUrl, extractors, 1);
        Parser parser = new HtmlParser(htmlParserParams);

        OfflinePathResolver offlinePathResolver = new OfflinePathResolverImpl();

        CopyPackagerParams copyPackagerParams = new CopyPackagerParamsImpl(outletDir, offlinePathResolver, storage);
        Packager htmlPackager = new CopyPackager(copyPackagerParams);
        UuidLinkPackagerParams uuidLinkPackagerParams = new UuidLinkPackagerParamsImpl(outletDir, offlinePathResolver, storage);
        Packager uuidLinkPackager = new UuidLinkPackager(uuidLinkPackagerParams);

        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(rootPageUrl, downloader, storage,
                threadPool, headRetriever, Collections.singletonList(parser));
        Job downloadJob = new DownloadJob(downloadJobParams);
        downloadJob.process();

        PackageJobParams packageJobParams = new PackageJobParamsImpl(storage, htmlPackager, uuidLinkPackager, threadPool);
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
