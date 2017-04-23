package ru.yaal.offlinewebsite;

import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.job.Job;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.CopyPackagerParams;
import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.params.HtmlParserParams;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.params.RootLink;
import ru.yaal.offlinewebsite.api.params.SkipParserParams;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;
import ru.yaal.offlinewebsite.api.params.UuidLinkPackagerParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.filter.NestedPathFilter;
import ru.yaal.offlinewebsite.impl.filter.SameHostFilter;
import ru.yaal.offlinewebsite.impl.filter.SizeFilter;
import ru.yaal.offlinewebsite.impl.http.HeadRetrieverImpl;
import ru.yaal.offlinewebsite.impl.job.DownloadJob;
import ru.yaal.offlinewebsite.impl.job.PackageJob;
import ru.yaal.offlinewebsite.impl.packager.CopyPackager;
import ru.yaal.offlinewebsite.impl.packager.UuidLinkPackager;
import ru.yaal.offlinewebsite.impl.params.CopyPackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloadJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HeadRequestParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HtmlParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PackageJobParamsImpl;
import ru.yaal.offlinewebsite.impl.params.LinkImpl;
import ru.yaal.offlinewebsite.impl.params.SkipParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ThreadPoolParamsImpl;
import ru.yaal.offlinewebsite.impl.params.UuidLinkPackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.HtmlParser;
import ru.yaal.offlinewebsite.impl.parser.SkipParser;
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
        RootLink rootLink = new LinkImpl(url.toString());
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
        HtmlParserParams<TagNode> htmlHtmlParserParams = new HtmlParserParamsImpl<>(storage, rootLink, extractors, 1);
        Parser htmlParser = new HtmlParser(htmlHtmlParserParams);

        SkipParserParams skipParserParams = new SkipParserParamsImpl(storage, 0);
        SkipParser skipParser = new SkipParser(skipParserParams);

        OfflinePathResolver offlinePathResolver = new OfflinePathResolverImpl();

        CopyPackagerParams copyPackagerParams = new CopyPackagerParamsImpl(outletDir, offlinePathResolver, storage, 0);
        Packager copyPackager = new CopyPackager(copyPackagerParams);
        UuidLinkPackagerParams uuidLinkPackagerParams = new UuidLinkPackagerParamsImpl(outletDir, offlinePathResolver, storage, 1);
        Packager uuidLinkPackager = new UuidLinkPackager(uuidLinkPackagerParams);
        List<Packager> packagers = Arrays.asList(copyPackager, uuidLinkPackager);

        List<HeadingResFilter> headingFilters = Arrays.asList(
                new SameHostFilter(rootLink, true),
                new NestedPathFilter(rootLink, true));

        List<HeadedResFilter> headedFilters = Collections.singletonList(
                new SizeFilter(3_000_000, true)
        );

        List<Parser> parsers = Arrays.asList(htmlParser, skipParser);

        DownloadJobParams downloadJobParams = new DownloadJobParamsImpl(rootLink, downloader, storage,
                threadPool, headRetriever, parsers, headingFilters, headedFilters);
        Job downloadJob = new DownloadJob(downloadJobParams);
        downloadJob.process();

        PackageJobParams packageJobParams = new PackageJobParamsImpl(storage, packagers, threadPool);
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
