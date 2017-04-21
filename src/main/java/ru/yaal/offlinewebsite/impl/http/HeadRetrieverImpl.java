package ru.yaal.offlinewebsite.impl.http;

import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.http.HeadRetriever;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class HeadRetrieverImpl implements HeadRetriever {
    private final Network network;
    private final Storage storage;

    public HeadRetrieverImpl(HeadRequestParams params) {
        network = params.getNetwork();
        storage = params.getStorage();
    }

    @Override
    public ResourceId<HeadedRes> requestHead(ResourceId<HeadingRes> hingResId) {
        HeadingRes hingRes = storage.getResource(hingResId);
        HttpInfo httpInfo = network.requestHttpInfo(hingRes.getUrl());
        ResourceId<HeadedRes> hedResId = storage.createHeadedResource(hingResId, httpInfo);
        log.debug("{} -> {}", hedResId, httpInfo);
        return hedResId;
    }
}
