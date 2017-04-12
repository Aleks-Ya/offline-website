package ru.yaal.offlinewebsite.impl.storage;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;
import ru.yaal.offlinewebsite.api.resource.NewResource;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
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
    private final Map<DownloadingResource.Id, ByteArrayOutputStream> dingRess = new HashMap<>();

    @Override
    public <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id) {
        return (R) data.get(id);
    }

    @Override
    public synchronized NewResource.NewResourceId createNewResource(SiteUrl url) {
        NewResource.NewResourceId newResId = new NewResource.NewResourceId(url.getUrl());
        checkAlreadyExists(newResId);
        NewResource newRes = new NewResource<>(newResId, url);
        data.put(newRes.getId(), newRes);
        return newRes.getId();
    }

    @Override
    public synchronized DownloadingResourceImpl.Id createDownloadingResource(NewResource.NewResourceId newResId) {
        NewResource newRes = (NewResource) data.get(newResId);
        DownloadingResource.Id id = new DownloadingResource.Id(newResId.getId());
        checkAlreadyExists(id);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dingRess.put(id, os);
        DownloadingResource dingResource = new DownloadingResourceImpl<>(id, newRes.getUrl(), os);
        data.remove(newResId);
        data.put(dingResource.getId(), dingResource);
        return dingResource.getId();
    }

    @Override
    @SneakyThrows
    public synchronized BytesDownloadedResource.Id createDownloadedResource(DownloadingResourceImpl.Id dingResId) {
        BytesDownloadedResource.Id id = new BytesDownloadedResource.Id(dingResId.getId());
        checkAlreadyExists(id);
        byte[] bytes = dingRess.get(dingResId).toByteArray();
        DownloadedResource dedResource = new BytesDownloadedResource<>(id, bytes);
        data.remove(dingResId);
        data.put(id, dedResource);
        return id;
    }

    private void checkAlreadyExists(Resource.ResourceId id) {
        if (data.containsKey(id)) {
            throw new ResourceAlreadyExistsException(id);
        }
    }
}
