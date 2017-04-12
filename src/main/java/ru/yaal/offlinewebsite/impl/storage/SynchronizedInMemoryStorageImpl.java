package ru.yaal.offlinewebsite.impl.storage;

import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class SynchronizedInMemoryStorageImpl implements Storage {
    private final Map<Resource.ResourceId, Resource> data = new HashMap<>();

    @Override
    public <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id) {
        return (R) data.get(id);
    }

    @Override
    public synchronized NewResource.NewResourceId createNewResource(SiteUrl url) {
        NewResource newResource = new NewResource<>(new NewResource.NewResourceId(url.getUrl()));
        data.put(newResource.getId(), newResource);
        return newResource.getId();
    }

    @Override
    public synchronized DownloadedResource.Id createDownloadedResource(
            DownloadingResource.Id downloadingResourceId) {
        DownloadedResource.Id id = new DownloadedResource.Id(downloadingResourceId.getId());
        DownloadedResource downloadedResource = new DownloadedResource<>(id);
        data.remove(downloadingResourceId);
        data.put(id, downloadedResource);
        return id;

    }

    @Override
    public synchronized DownloadingResource.Id createDownloadingResource(
            NewResource.NewResourceId newResourceId) {
        DownloadingResource.Id id = new DownloadingResource.Id(newResourceId.getId());
        OutputStream os = new ByteArrayOutputStream();
        DownloadingResource downloadingResource = new DownloadingResource<>(id, os);
        data.remove(newResourceId);
        data.put(downloadingResource.getId(), downloadingResource);
        return downloadingResource.getId();
    }
}
