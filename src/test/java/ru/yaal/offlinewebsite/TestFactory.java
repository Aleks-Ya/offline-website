package ru.yaal.offlinewebsite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.CopyPackagerParams;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.params.HtmlParserParams;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.params.SkipParserParams;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.params.UuidLinkPackagerParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.task.Task;
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HeadRetrieverImpl;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.packager.CopyPackager;
import ru.yaal.offlinewebsite.impl.packager.UuidLinkPackager;
import ru.yaal.offlinewebsite.impl.params.CopyPackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloadTaskParamsImpl;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HeadRequestParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HtmlParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SkipParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.params.UuidLinkPackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.HtmlParser;
import ru.yaal.offlinewebsite.impl.parser.SkipParser;
import ru.yaal.offlinewebsite.impl.parser.TagAttributeExtractor;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;
import ru.yaal.offlinewebsite.impl.task.DownloadTask;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class TestFactory {
    public static final HttpInfo httpInfoDefault
            = new HttpInfoImpl(200, 10_000, 6000000, "text/html");
    public static final List<UrlExtractor<TagNode>> allExtractors = Arrays.asList(
            new TagAttributeExtractor(new TagAttributeExtractor.Params("a", "href")),
            new TagAttributeExtractor(new TagAttributeExtractor.Params("link", "href")),
            new TagAttributeExtractor(new TagAttributeExtractor.Params("script", "href")));
    private final Path outletDir;
    private final Storage storage;
    private final Downloader downloader;
    private final Parser htmlParser;
    private final Parser skipParser;
    private final Packager copyPackager;
    private final Packager uuidLinkPackager;
    private final BytesNetwork network;
    private final HeadRetriever headRetriever;
    private final List<Parser> allParsers;
    private final List<Packager> allPackagers;

    public TestFactory(RootPageUrl rootPageUrl) {
        this(rootPageUrl, makTempDir());
    }

    @SneakyThrows
    private static Path makTempDir() {
        return Files.createTempDirectory(TestFactory.class.getSimpleName());
    }

    public TestFactory(RootPageUrl rootPageUrl, Path outletDir) {
        this.outletDir = outletDir;
        StorageParams storageParams = new StorageParamsImpl();
        storage = new SyncInMemoryStorageImpl(storageParams);

        network = new BytesNetwork();
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        headRetriever = new HeadRetrieverImpl(headRequestParams);

        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network);
        downloader = new DownloaderImpl(downloaderParams);

        HtmlParserParams<TagNode> htmlHtmlParserParams = new HtmlParserParamsImpl<>(storage, rootPageUrl, TestFactory.allExtractors, 1);
        htmlParser = new HtmlParser(htmlHtmlParserParams);

        SkipParserParams skipParserParams = new SkipParserParamsImpl(storage, 1);
        skipParser = new SkipParser(skipParserParams);

        allParsers = Arrays.asList(htmlParser, skipParser);

        CopyPackagerParams params = new CopyPackagerParamsImpl(outletDir, new OfflinePathResolverImpl(), storage, 0);
        copyPackager = new CopyPackager(params);

        UuidLinkPackagerParams isPackagerParams = new UuidLinkPackagerParamsImpl(outletDir, new OfflinePathResolverImpl(), storage, 1);
        uuidLinkPackager = new UuidLinkPackager(isPackagerParams);

        allPackagers = Arrays.asList(copyPackager, uuidLinkPackager);
    }

    public ResourceId<NewRes> createNewRes(PageUrl pageUrl) {
        return storage.createNewResource(pageUrl);
    }

    public ResourceId<HeadingRes> createHeadingRes(PageUrl pageUrl, HttpInfo httpInfo) {
        network.putHttpInfo(pageUrl, httpInfo);
        return storage.createHeadingResource(createNewRes(pageUrl));
    }

    public ResourceId<HeadedRes> createHeadedRes(PageUrl pageUrl, HttpInfo httpInfo) {
        ResourceId<HeadingRes> headingResId = createHeadingRes(pageUrl, httpInfo);
        return storage.createHeadedResource(headingResId, httpInfo);
    }

    public ResourceId<DownloadingRes> createDownloadingRes(PageUrl pageUrl, String html, HttpInfo httpInfo) {
        ResourceId<HeadedRes> headedResId = createHeadedRes(pageUrl, httpInfo);
        network.putBytes(pageUrl, html);
        return storage.createDownloadingResource(headedResId);
    }

    public ResourceId<DownloadedRes> createDownloadedRes(PageUrl pageUrl, String html, HttpInfo httpInfo) {
        return downloader.download(createDownloadingRes(pageUrl, html, httpInfo));
    }

    public ResourceId<ParsingRes> createParsingRes(PageUrl pageUrl, String html, HttpInfo httpInfo) {
        ResourceId<DownloadedRes> downloadedResId = createDownloadedRes(pageUrl, html, httpInfo);
        return storage.createParsingRes(downloadedResId);
    }

    public ResourceId<PackagingRes> createParsedRes(ResourceId<NewRes> newResId, String html, HttpInfo httpInfo) {
        NewRes newRes = storage.getResource(newResId);
        PageUrl pageUrl = newRes.getUrl();

        network.putHttpInfo(pageUrl, httpInfo);
        ResourceId<HeadingRes> headingResId = storage.createHeadingResource(newResId);

        ResourceId<HeadedRes> headedResId = storage.createHeadedResource(headingResId, httpInfo);

        network.putBytes(pageUrl, html);
        ResourceId<DownloadingRes> downloadingResId = storage.createDownloadingResource(headedResId);

        ResourceId<DownloadedRes> downloadedResId = downloader.download(downloadingResId);

        ResourceId<ParsingRes> parsingResId = storage.createParsingRes(downloadedResId);

        ResourceId<ParsedRes> parsedResId = htmlParser.parse(parsingResId);

        return storage.createPackagingRes(parsedResId);
    }

    public ResourceId<ParsedRes> createParsedRes(PageUrl pageUrl, String html, HttpInfo httpInfo) {
        return htmlParser.parse(createParsingRes(pageUrl, html, httpInfo));
    }

    public ResourceId<PackagingRes> createPackagingRes(PageUrl pageUrl, String html, HttpInfo httpInfo) {
        ResourceId<ParsedRes> parsedResId = createParsedRes(pageUrl, html, httpInfo);
        return storage.createPackagingRes(parsedResId);
    }

    public ResourceId<PackagedRes> createPackagedRes(PageUrl pageUrl, String html, HttpInfo httpInfo) {
        return copyPackager.pack(createPackagingRes(pageUrl, html, httpInfo));
    }

    public Task createTask(RootPageUrl rootPageUrl, ResourceId<HeadingRes> hingResId, boolean onlySameDomain, long maxSize) {
        DownloadTaskParams downloadTaskParams = new DownloadTaskParamsImpl(rootPageUrl, hingResId, downloader, storage,
                onlySameDomain, headRetriever, maxSize, Collections.singletonList(htmlParser));
        return new DownloadTask(downloadTaskParams);
    }


}
