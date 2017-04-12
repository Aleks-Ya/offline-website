package ru.yaal.offlinewebsite.api.storage;

import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;

/**
 * @author Aleksey Yablokov
 */
public interface Storage {
    <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id);

    NewResource.NewResourceId createNewResource(SiteUrl url);

    BytesDownloadedResource.Id createDownloadedResource(
            DownloadingResourceImpl.Id downloadingResourceId);

    DownloadingResourceImpl.Id createDownloadingResource(NewResource.NewResourceId newResId);
}
