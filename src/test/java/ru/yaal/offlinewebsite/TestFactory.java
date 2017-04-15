package ru.yaal.offlinewebsite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.params.PackagerParams;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.params.TaskParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
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
import ru.yaal.offlinewebsite.impl.http.HeadRequestImpl;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.packager.FolderPackager;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.HeadRequestParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.params.TaskParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;
import ru.yaal.offlinewebsite.impl.task.TaskImpl;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class TestFactory {
    public static final HttpInfo httpInfoDefault = new HttpInfoImpl(200, 10_000, 6000000);
    private final Path outletDir;
    private final Storage storage;
    private final Downloader downloader;
    private final Parser<TagNode> parser;
    private final Packager<TagNode> packager;
    private final BytesNetwork network;
    private final HeadRequest headRequest;

    public TestFactory(SiteUrl rootSiteUrl) {
        this(rootSiteUrl, makTempDir());
    }

    @SneakyThrows
    private static Path makTempDir() {
        return Files.createTempDirectory(TestFactory.class.getSimpleName());
    }

    public TestFactory(SiteUrl rootSiteUrl, Path outletDir) {
        this.outletDir = outletDir;
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        storage = new SyncInMemoryStorageImpl(storageParams);

        network = new BytesNetwork();
        HeadRequestParams headRequestParams = new HeadRequestParamsImpl(storage, network);
        headRequest = new HeadRequestImpl(headRequestParams);

        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network);
        downloader = new DownloaderImpl(downloaderParams);

        ParserParams parserParams = new ParserParamsImpl(storage, rootSiteUrl);
        parser = new ParserImpl(parserParams);

        PackagerParams params = new PackagerParamsImpl(outletDir, new OfflinePathResolverImpl(), storage);
        packager = new FolderPackager(params);
    }

    public ResourceId<NewRes> createNewRes(SiteUrl siteUrl) {
        return storage.createNewResource(siteUrl);
    }

    public ResourceId<HeadingRes> createHeadingRes(SiteUrl siteUrl, HttpInfo httpInfo) {
        network.putHttpInfo(siteUrl, httpInfo);
        return storage.createHeadingResource(createNewRes(siteUrl));
    }

    public ResourceId<HeadedRes> createHeadedRes(SiteUrl siteUrl, HttpInfo httpInfo) {
        ResourceId<HeadingRes> hingResId = createHeadingRes(siteUrl, httpInfo);
        return storage.createHeadedResource(hingResId, httpInfo);
    }

    public ResourceId<DownloadingRes> createDownloadingRes(SiteUrl siteUrl, String html, HttpInfo httpInfo) {
        ResourceId<HeadedRes> headedRes = createHeadedRes(siteUrl, httpInfo);
        network.putBytes(siteUrl, html);
        return storage.createDownloadingResource(headedRes);
    }

    public ResourceId<DownloadedRes> createDownloadedRes(SiteUrl siteUrl, String html, HttpInfo httpInfo) {
        return downloader.download(createDownloadingRes(siteUrl, html, httpInfo));
    }

    public ResourceId<ParsingRes<TagNode>> createParsingRes(SiteUrl siteUrl, String html, HttpInfo httpInfo) {
        ResourceId<DownloadedRes> dedResId = createDownloadedRes(siteUrl, html, httpInfo);
        return storage.createParsingRes(dedResId);
    }

    public ResourceId<ParsedRes<TagNode>> createParsedRes(SiteUrl siteUrl, String html, HttpInfo httpInfo) {
        return parser.parse(createParsingRes(siteUrl, html, httpInfo));
    }

    public ResourceId<PackagingRes<TagNode>> createPackagingRes(SiteUrl siteUrl, String html, HttpInfo httpInfo) {
        ResourceId<ParsedRes<TagNode>> parsedResId = createParsedRes(siteUrl, html, httpInfo);
        return storage.createPackagingRes(parsedResId);
    }

    public ResourceId<PackagedRes> createPackagedRes(SiteUrl siteUrl, String html, HttpInfo httpInfo) {
        return packager.pack(createPackagingRes(siteUrl, html, httpInfo));
    }

    public Task createTask(SiteUrl rootSiteUrl, ResourceId<HeadingRes> hingResId, boolean onlySameDomain, long maxSize) {
        TaskParams taskParams = new TaskParamsImpl(rootSiteUrl, hingResId, downloader, storage,
                onlySameDomain, headRequest, maxSize, parser);
        return new TaskImpl(taskParams);
    }


}
