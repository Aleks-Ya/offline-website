package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id);

    NewRes.Id createNewResource(SiteUrl url);

    HeadingRes.Id createHeadingResource(NewRes.Id newResId);

    HeadedRes.Id createHeadedResource(HeadingRes.Id hingResId, HttpInfo httpInfo);

    DownloadingRes.Id createDownloadingResource(HeadedRes.Id newResId);

    DownloadedRes.Id createDownloadedResource(DownloadingRes.Id dingResId);

    ParsingResource.Id createParsingRes(DownloadedRes.Id dedResId);

    ParsedResource.Id createParsedRes(ParsingResource.Id dedResId);

    RejectedRes.Id createRejectedRes(Resource.ResourceId resId);
}
