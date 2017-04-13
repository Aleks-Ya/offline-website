package ru.yaal.offlinewebsite.impl.http;

import ru.yaal.offlinewebsite.api.http.HeadRequest;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.resource.HttpHeadedResource;
import ru.yaal.offlinewebsite.api.resource.HttpHeadingResource;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;

/**
 * @author Aleksey Yablokov
 */
public class HeadRequestImpl implements HeadRequest {
    private final Network network;
    private final Storage storage;

    public HeadRequestImpl(HeadRequestParams params) {
        network = params.getNetwork();
        storage = params.getStorage();
    }

    @Override
    public HttpHeadedResource.Id requestHead(HttpHeadingResource.Id hingResId) {
        HttpHeadingResource<HttpHeadingResource.Id> hingRes = storage.getResource(hingResId);
        HttpInfo httpInfo = network.requestHttpInfo(hingRes.getUrl());
        return storage.createHeadedResource(hingResId, httpInfo);
    }
}
