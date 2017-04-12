package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.resource.Resource;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id);

    NewResource.NewResourceId createNewResource(SiteUrl url);

    DownloadedResource.Id createDownloadedResource(
            DownloadingResource.Id downloadingResourceId);

    DownloadingResource.Id createDownloadingResource(
            NewResource.NewResourceId newResourceId);
}
