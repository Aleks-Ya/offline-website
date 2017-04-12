package ru.yaal.offlinewebsite.impl.storage;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedResource;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResourceImpl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class SynchronizedInMemoryStorageImpl implements Storage {
    private final Map<Resource.ResourceId, Resource> data = new HashMap<>();
    private final Map<DownloadingResource.Id, ByteArrayOutputStream> downloadingResources = new HashMap<>();

    @Override
    public <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id) {
        return (R) data.get(id);
    }

    @Override
    public synchronized NewResource.NewResourceId createNewResource(SiteUrl url) {
        NewResource.NewResourceId id = new NewResource.NewResourceId(url.getUrl());
        NewResource newResource = new NewResource<>(id, url);
        data.put(newResource.getId(), newResource);
        return newResource.getId();
    }

    @Override
    public synchronized DownloadingResourceImpl.Id createDownloadingResource(
            NewResource.NewResourceId newResId) {
        NewResource newRes = (NewResource) data.get(newResId);
        DownloadingResource.Id id = new DownloadingResource.Id(newResId.getId());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        downloadingResources.put(id, os);
        DownloadingResource downloadingResource = new DownloadingResourceImpl<>(id, newRes.getUrl(), os);
        data.remove(newResId);
        data.put(downloadingResource.getId(), downloadingResource);
        return downloadingResource.getId();
    }

    @Override
    @SneakyThrows
    public synchronized BytesDownloadedResource.Id createDownloadedResource(
            DownloadingResourceImpl.Id downloadingResId) {
        BytesDownloadedResource.Id id = new BytesDownloadedResource.Id(downloadingResId.getId());
        byte[] bytes = downloadingResources.get(downloadingResId).toByteArray();
        DownloadedResource downloadedResource = new BytesDownloadedResource<>(id, bytes);
        data.remove(downloadingResId);
        data.put(id, downloadedResource);
        return id;

    }

}
