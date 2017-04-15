package ru.yaal.offlinewebsite.impl;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.downloader.Downloader;
import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
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
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceComparator;
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
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.params.StorageParamsImpl;
import ru.yaal.offlinewebsite.impl.params.TaskParamsImpl;
import ru.yaal.offlinewebsite.impl.parser.ParserImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparatorImpl;
import ru.yaal.offlinewebsite.impl.storage.SyncInMemoryStorageImpl;
import ru.yaal.offlinewebsite.impl.system.BytesNetwork;
import ru.yaal.offlinewebsite.impl.task.TaskImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public abstract class TestHelper {
    public static Task makeTask(String rootUrl, String html, int responseCode,
                                int contentLength, int lastModified, boolean onlySameDomain, int maxSize) {

        SiteUrl rootSiteUrl = new SiteUrlImpl(rootUrl);
        StorageParams storageParams = new StorageParamsImpl(new ResourceComparatorImpl());
        Storage storage = new SyncInMemoryStorageImpl(storageParams);

        ResourceId<NewRes> newResId = storage.createNewResource(rootSiteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
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
        TaskParams taskParams = new TaskParamsImpl(rootSiteUrl, hingResId, downloader, storage,
                onlySameDomain, headRequest, maxSize, parser);
        return new TaskImpl(taskParams);
    }

    public static Packager<TagNode> makePackager(Path outletDir) {
        OfflinePathResolver resolver = new OfflinePathResolverImpl();
        ResourceComparator comparator = new ResourceComparatorImpl();
        StorageParams storageParams = new StorageParamsImpl(comparator);
        Storage storage = new SyncInMemoryStorageImpl(storageParams);
        PackagerParams params = new PackagerParamsImpl(outletDir, resolver, storage);
        return new FolderPackager(params);
    }

    public static ResourceId<ParsedRes> makeParsedRes(Storage storage, SiteUrl siteUrl, Parser parser, int responseCode,
                                                      int contentLength, int lastModified, String html)
            throws IOException {
        ResourceId<NewRes> newResId = storage.createNewResource(siteUrl);
        ResourceId<HeadingRes> hingResId = storage.createHeadingResource(newResId);
        HttpInfo httpInfo = new HttpInfoImpl(responseCode, contentLength, lastModified);
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        ResourceId<DownloadingRes> dingResId = storage.createDownloadingResource(hedResId);
        DownloadingRes dingRes = storage.getResource(dingResId);
        OutputStream os = dingRes.getOutputStream();
        IOUtils.write(html.getBytes(), os);
        ResourceId<DownloadedRes> dedResId = storage.createDownloadedResource(dingResId);
        ResourceId<ParsingRes<TagNode>> pingResId = storage.createParsingRes(dedResId);
        return parser.parse(pingResId);
    }
}
