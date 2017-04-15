package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
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

import java.nio.file.Path;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <R extends Resource> boolean hasResource(ResourceId<R> resId);

    <R extends Resource> R getResource(ResourceId<R> resId);

    ResourceId<NewRes> createNewResource(SiteUrl url);

    ResourceId<HeadingRes> createHeadingResource(ResourceId<NewRes> newResId);

    ResourceId<HeadedRes> createHeadedResource(ResourceId<HeadingRes> hingResId, HttpInfo httpInfo);

    ResourceId<DownloadingRes> createDownloadingResource(ResourceId<HeadedRes> newResId);

    ResourceId<DownloadedRes> createDownloadedResource(ResourceId<DownloadingRes> dingResId);

    <C> ResourceId<ParsingRes<C>> createParsingRes(ResourceId<DownloadedRes> dedResId);

    <C> ResourceId<ParsedRes<C>> createParsedRes(ResourceId<ParsingRes<C>> dedResId);

    <C> ResourceId<PackagingRes<C>> createPackagingRes(ResourceId<ParsedRes<C>> pedResId);

    <C> ResourceId<PackagedRes> createPackagedRes(ResourceId<PackagingRes<C>> packagingResId, Path location);

    ResourceId<RejectedRes> createRejectedRes(ResourceId<?> resId);

    List<ResourceId<NewRes>> getNewResourceIds();
}
