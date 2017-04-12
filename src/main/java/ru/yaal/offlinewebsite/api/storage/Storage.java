package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id);

    NewResource.Id createNewResource(SiteUrl url);

    BytesDownloadedResource.Id createDownloadedResource(DownloadingResource.Id dingResId);

    DownloadingResource.Id createDownloadingResource(NewResource.Id newResId);
}
