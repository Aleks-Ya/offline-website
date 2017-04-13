package ru.yaal.offlinewebsite.impl.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;
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
@Slf4j
public class SyncInMemoryStorageImpl implements Storage {
    private final Map<Resource.ResourceId, Resource> data = new HashMap<>();
    private final Map<DownloadingResource.Id, ByteArrayOutputStream> dingRess = new HashMap<>();

    @Override
    public <ID extends Resource.ResourceId, R extends Resource<ID>> R getResource(ID id) {
        return (R) data.get(id);
    }

    @Override
    public synchronized NewResource.Id createNewResource(SiteUrl url) {
        NewResource.Id newResId = new NewResource.Id(url.getUrl());
        checkAlreadyExists(newResId);
        NewResource newRes = new NewResource<>(newResId, url);
        data.put(newRes.getId(), newRes);
        log.debug("NewResource is created: " + newResId);
        return newResId;
    }

    @Override
    public HttpHeadingResource.Id createHeadingResource(NewResource.Id newResId) {
        return null;
    }

    @Override
    public HttpHeadedResource.Id createHeadedResource(HttpHeadingResource.Id hingResId, HttpInfo httpInfo) {
        return null;
    }

    @Override
    public synchronized DownloadingResource.Id createDownloadingResource(NewResource.Id newResId) {
        NewResource newRes = (NewResource) data.get(newResId);
        DownloadingResource.Id dingResId = new DownloadingResource.Id(newResId.getId());
        checkAlreadyExists(dingResId);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dingRess.put(dingResId, os);
        DownloadingResource dingRes = new DownloadingResourceImpl<>(dingResId, newRes.getUrl(), os);
        data.remove(newResId);
        data.put(dingRes.getId(), dingRes);
        log.debug("DownloadingResource is created: " + dingResId);
        return dingResId;
    }

    @Override
    @SneakyThrows
    public synchronized DownloadedResource.Id createDownloadedResource(DownloadingResource.Id dingResId) {
        DownloadedResource.Id dedResId = new BytesDownloadedResource.Id(dingResId.getId());
        checkAlreadyExists(dedResId);
        DownloadingResource<DownloadingResource.Id> dingRes = getResource(dingResId);
        byte[] bytes = dingRess.get(dingResId).toByteArray();
        DownloadedResource dedResource = new BytesDownloadedResource<>(dedResId, dingRes.getUrl(), bytes);
        data.remove(dingResId);
        data.put(dedResId, dedResource);
        log.debug("BytesDownloadedResource is created: " + dedResId);
        return dedResId;
    }

    @Override
    public ParsingResource.Id createParsingRes(DownloadedResource.Id dedResId) {
        return null;
    }

    @Override
    public ParsedResource.Id createParsedRes(ParsingResource.Id dedResId) {
        return null;
    }

    private void checkAlreadyExists(Resource.ResourceId id) {
        if (data.containsKey(id)) {
            throw new ResourceAlreadyExistsException(id);
        }
    }
}
