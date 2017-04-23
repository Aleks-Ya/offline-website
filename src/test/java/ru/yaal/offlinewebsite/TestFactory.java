package ru.yaal.offlinewebsite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.CopyPackagerParams;
import ru.yaal.offlinewebsite.api.params.DownloadTaskParams;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.params.HtmlParserParams;
import ru.yaal.offlinewebsite.api.params.Link;
import ru.yaal.offlinewebsite.api.params.RootLink;
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
import ru.yaal.offlinewebsite.impl.filter.NestedPathFilter;
import ru.yaal.offlinewebsite.impl.filter.SameHostFilter;
import ru.yaal.offlinewebsite.impl.filter.SizeFilter;
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
    public final List<HeadingResFilter> headingFilters;

    public final List<HeadedResFilter> headedFilters;
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

    public TestFactory(RootLink rootLink) {
        this(rootLink, makTempDir());
    }

    @SneakyThrows
    private static Path makTempDir() {
        return Files.createTempDirectory(TestFactory.class.getSimpleName());
    }

    public TestFactory(RootLink rootLink, Path outletDir) {
        this.outletDir = outletDir;
        StorageParams storageParams = new StorageParamsImpl();
        storage = new SyncInMemoryStorageImpl(storageParams);

        headingFilters = Arrays.asList(
                new SameHostFilter(rootLink, true),
                new NestedPathFilter(rootLink, true));

        headedFilters = Collections.singletonList(
                new SizeFilter(3_000_000, true));

        network = new BytesNetwork();
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        headRetriever = new HeadRetrieverImpl(headRequestParams);

        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network);
        downloader = new DownloaderImpl(downloaderParams);

        HtmlParserParams<TagNode> htmlHtmlParserParams = new HtmlParserParamsImpl<>(storage, rootLink, TestFactory.allExtractors, 1);
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

    public ResourceId<NewRes> createNewRes(Link link) {
        return storage.createNewResource(link);
    }

    public ResourceId<HeadingRes> createHeadingRes(Link link, HttpInfo httpInfo) {
        network.putHttpInfo(link, httpInfo);
        return storage.createHeadingResource(createNewRes(link));
    }

    public ResourceId<HeadedRes> createHeadedRes(Link link, HttpInfo httpInfo) {
        ResourceId<HeadingRes> headingResId = createHeadingRes(link, httpInfo);
        return storage.createHeadedResource(headingResId, httpInfo);
    }

    public ResourceId<DownloadingRes> createDownloadingRes(Link link, String html, HttpInfo httpInfo) {
        ResourceId<HeadedRes> headedResId = createHeadedRes(link, httpInfo);
        network.putBytes(link, html);
        return storage.createDownloadingResource(headedResId);
    }

    public ResourceId<DownloadedRes> createDownloadedRes(Link link, String html, HttpInfo httpInfo) {
        return downloader.download(createDownloadingRes(link, html, httpInfo));
    }

    public ResourceId<ParsingRes> createParsingRes(Link link, String html, HttpInfo httpInfo) {
        ResourceId<DownloadedRes> downloadedResId = createDownloadedRes(link, html, httpInfo);
        return storage.createParsingRes(downloadedResId);
    }

    public ResourceId<PackagingRes> createParsedRes(ResourceId<NewRes> newResId, String html, HttpInfo httpInfo) {
        NewRes newRes = storage.getResource(newResId);
        Link link = newRes.getUrl();

        network.putHttpInfo(link, httpInfo);
        ResourceId<HeadingRes> headingResId = storage.createHeadingResource(newResId);

        ResourceId<HeadedRes> headedResId = storage.createHeadedResource(headingResId, httpInfo);

        network.putBytes(link, html);
        ResourceId<DownloadingRes> downloadingResId = storage.createDownloadingResource(headedResId);

        ResourceId<DownloadedRes> downloadedResId = downloader.download(downloadingResId);

        ResourceId<ParsingRes> parsingResId = storage.createParsingRes(downloadedResId);

        ResourceId<ParsedRes> parsedResId = htmlParser.parse(parsingResId);

        return storage.createPackagingRes(parsedResId);
    }

    public ResourceId<ParsedRes> createParsedRes(Link link, String html, HttpInfo httpInfo) {
        return htmlParser.parse(createParsingRes(link, html, httpInfo));
    }

    public ResourceId<PackagingRes> createPackagingRes(Link link, String html, HttpInfo httpInfo) {
        ResourceId<ParsedRes> parsedResId = createParsedRes(link, html, httpInfo);
        return storage.createPackagingRes(parsedResId);
    }

    public ResourceId<PackagedRes> createPackagedRes(Link link, String html, HttpInfo httpInfo) {
        return copyPackager.pack(createPackagingRes(link, html, httpInfo));
    }

    public Task createDownloadTask(RootLink rootLink, ResourceId<HeadingRes> hingResId) {
        DownloadTaskParams downloadTaskParams = new DownloadTaskParamsImpl(rootLink, hingResId, downloader, storage,
                headRetriever, Collections.singletonList(htmlParser), headingFilters, headedFilters);
        return new DownloadTask(downloadTaskParams);
    }


}
