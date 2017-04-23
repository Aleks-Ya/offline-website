package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.parser.UuidLink;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.statistics.Statistics;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <R extends Resource> boolean hasResource(ResourceId<R> resId);

    <R extends Resource> R getResource(ResourceId<R> resId);

    ResourceId<NewRes> createNewResource(PageUrl url);

    ResourceId<HeadingRes> createHeadingResource(ResourceId<NewRes> newResId);

    ResourceId<HeadedRes> createHeadedResource(ResourceId<HeadingRes> hingResId, HttpInfo httpInfo);

    ResourceId<DownloadingRes> createDownloadingResource(ResourceId<HeadedRes> newResId);

    ResourceId<DownloadedRes> createDownloadedResource(ResourceId<DownloadingRes> dingResId);

    ResourceId<ParsingRes> createParsingRes(ResourceId<DownloadedRes> dedResId);

    ResourceId<ParsedRes> createParsedRes(ResourceId<ParsingRes> parsingResId, InputStream content, List<UuidLink> links);

    ResourceId<PackagingRes> createPackagingRes(ResourceId<ParsedRes> pedResId);

    ResourceId<PackagedRes> createPackagedRes(ResourceId<PackagingRes> packagingResId, Path location);

    ResourceId<RejectedRes> createRejectedRes(ResourceId<?> resId);

    List<ResourceId<NewRes>> getNewResourceIds();

    List<ResourceId<ParsedRes>> getParsedResourceIds();

    //TODO delete Storage#getRejectedResourceIds() if unused
    List<ResourceId<RejectedRes>> getRejectedResourceIds();

    Statistics getStatistics();
}
