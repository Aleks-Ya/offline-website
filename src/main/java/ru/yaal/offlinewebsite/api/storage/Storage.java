package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;

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

    ResourceId<ParsingRes> createParsingRes(ResourceId<DownloadedRes> dedResId);

    ResourceId<ParsedRes> createParsedRes(ResourceId<ParsingRes> dedResId);

    ResourceId<RejectedRes> createRejectedRes(ResourceId<?> resId);

    List<ResourceId<NewRes>> getNewResourceIds();
}
