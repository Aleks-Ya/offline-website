package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id);

    NewResource.Id createNewResource(SiteUrl url);

    HttpHeadingResource.Id createHeadingResource(NewResource.Id newResId);

    HttpHeadedResource.Id createHeadedResource(HttpHeadingResource.Id hingResId, HttpInfo httpInfo);

    DownloadingResource.Id createDownloadingResource(NewResource.Id newResId);

    DownloadedResource.Id createDownloadedResource(DownloadingResource.Id dingResId);

    ParsingResource.Id createParsingRes(DownloadedResource.Id dedResId);

    ParsedResource.Id createParsedRes(ParsingResource.Id dedResId);
}
