package ru.yaal.offlinewebsite;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolverImpl;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.DownloaderParams;
import ru.yaal.offlinewebsite.api.params.PackagerParams;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
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
import ru.yaal.offlinewebsite.impl.downloader.DownloaderImpl;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;
import ru.yaal.offlinewebsite.impl.packager.FolderPackager;
import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;
import ru.yaal.offlinewebsite.impl.params.PackagerParamsImpl;
import ru.yaal.offlinewebsite.impl.params.ParserParamsImpl;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class TestFactory {
    private final Path outletDir;
    private final Storage storage;
    private final Downloader downloader;
    private final Parser parser;
    private final Packager<TagNode> packager;
    private final BytesNetwork network;

    public TestFactory(SiteUrl rootSiteUrl, String html) throws IOException {
        this(rootSiteUrl, 200, 100_000, 6000000000L, html,
                Files.createTempDirectory(TestFactory.class.getSimpleName()));

    }

    public TestFactory(
            SiteUrl rootSiteUrl, int responseCode, int contentLength, long lastModified, String html, Path outletDir)
            throws IOException {

        this.outletDir = outletDir;
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        storage = new SyncInMemoryStorageImpl(storageParams);

        PackagerParams params = new PackagerParamsImpl(outletDir, new OfflinePathResolverImpl(), storage);
        packager = new FolderPackager(params);
        network = new BytesNetwork();
        DownloaderParams downloaderParams = new DownloaderParamsImpl(storage, network);
        downloader = new DownloaderImpl(downloaderParams);
        ParserParams parserParams = new ParserParamsImpl(storage, rootSiteUrl);
        parser = new ParserImpl(parserParams);
    }

    public ResourceId<NewRes> createNewRes(String siteUrlStr) {
        SiteUrl url = new SiteUrlImpl(siteUrlStr);
        return storage.createNewResource(url);
    }

    public ResourceId<HeadingRes> createHeadingRes(String siteUrlStr) {
        return storage.createHeadingResource(createNewRes(siteUrlStr));
    }

    public ResourceId<HeadedRes> createHeadedRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        SiteUrl siteUrl = new SiteUrlImpl(siteUrlStr);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(createNewRes(siteUrlStr));
        HttpInfo httpInfo = new HttpInfoImpl(responseCode, contentLength, lastModified);
        network.putBytes(siteUrl, html);
//        network.putHttpInfo(siteUrl, httpInfo);
        return storage.createHeadedResource(hingResId, httpInfo);
    }

    public ResourceId<DownloadingRes> createDownloadingRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        ResourceId<HeadedRes> headedRes = createHeadedRes(siteUrlStr, html, responseCode, contentLength, lastModified);
        return storage.createDownloadingResource(headedRes);
    }

    public ResourceId<DownloadedRes> createDownloadedRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        ResourceId<DownloadingRes> dingResId = createDownloadingRes(siteUrlStr, html, responseCode, contentLength, lastModified);
        return storage.createDownloadedResource(dingResId);
    }

    public ResourceId<ParsingRes<TagNode>> createParsingRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        ResourceId<DownloadedRes> dedResId = createDownloadedRes(siteUrlStr, html, responseCode, contentLength, lastModified);
        return storage.createParsingRes(dedResId);
    }

    public ResourceId<ParsedRes<TagNode>> createParsedRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        ResourceId<ParsingRes<TagNode>> parsingResId = createParsingRes(siteUrlStr, html, responseCode, contentLength, lastModified);
        return storage.createParsedRes(parsingResId);
    }

    public ResourceId<PackagingRes<TagNode>> createPackagingRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        ResourceId<ParsedRes<TagNode>> parsedResId = createParsedRes(siteUrlStr, html, responseCode, contentLength, lastModified);
        return storage.createPackagingRes(parsedResId);
    }

    public ResourceId<PackagedRes> createPackagedRes(
            String siteUrlStr, String html, int responseCode, long contentLength, long lastModified) {
        ResourceId<PackagingRes<TagNode>> packagingResId = createPackagingRes(siteUrlStr, html, responseCode, contentLength, lastModified);
        return storage.createPackagedRes(packagingResId);
    }


}
