package ru.yaal.offlinewebsite.impl.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceComparator;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.resource.BytesDownloadedRes;
import ru.yaal.offlinewebsite.impl.resource.BytesParsedRes;
import ru.yaal.offlinewebsite.impl.resource.DownloadingResImpl;
import ru.yaal.offlinewebsite.impl.resource.HeadedResImpl;
import ru.yaal.offlinewebsite.impl.resource.HeadingResImpl;
import ru.yaal.offlinewebsite.impl.resource.NewResImpl;
import ru.yaal.offlinewebsite.impl.resource.PackagedResImpl;
import ru.yaal.offlinewebsite.impl.resource.PackagingResImpl;
import ru.yaal.offlinewebsite.impl.resource.ParsingResImpl;
import ru.yaal.offlinewebsite.impl.resource.RejectedResImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class SyncInMemoryStorageImpl implements Storage {
    private final Map<ResourceId, Resource> data = new HashMap<>();
    private final Map<ResourceId<DownloadingRes>, ByteArrayOutputStream> dingRess = new HashMap<>();
    private final ResourceComparator comparator;

    public SyncInMemoryStorageImpl(StorageParams params) {
        this.comparator = params.getResourceComparator();
    }

    @Override
    public synchronized <R extends Resource> R getResource(ResourceId<R> resId) {
        return (R) data.get(resId);
    }

    @Override
    public <R extends Resource> boolean hasResource(ResourceId<R> resId) {
        return getResource(resId) != null;
    }

    @Override
    public synchronized ResourceId<NewRes> createNewResource(SiteUrl url) {
        ResourceId<NewRes> newResId = new ResourceIdImpl<>(url.getUrl());
        if (hasResource(newResId)) {
            throw new ResourceAlreadyExistsException(newResId);
        }
        NewRes newRes = new NewResImpl(newResId, url);
        data.put(newRes.getId(), newRes);
        log.debug("NewRes is created: " + newResId);
        return newResId;
    }

    @Override
    public synchronized ResourceId<HeadingRes> createHeadingResource(ResourceId<NewRes> newResId) {
        checkAlreadyExists(newResId, HeadingRes.class);
        ResourceId<HeadingRes> hingResId = new ResourceIdImpl<>(newResId.getId());
        NewRes newRes = (NewRes) data.get(newResId);
        HeadingRes hingRes = new HeadingResImpl(hingResId, newRes.getUrl());
        data.remove(newResId);
        data.put(hingResId, hingRes);
        return hingResId;
    }

    @Override
    public synchronized ResourceId<HeadedRes> createHeadedResource(ResourceId<HeadingRes> hingResId, HttpInfo httpInfo) {
        checkAlreadyExists(hingResId, HeadedRes.class);
        ResourceId<HeadedRes> hedResId = new ResourceIdImpl<>(hingResId.getId());
        HeadingRes hingRes = (HeadingRes) data.get(hingResId);
        HeadedRes hedRes = new HeadedResImpl(hedResId, hingRes.getUrl(), httpInfo);
        data.remove(hingResId);
        data.put(hedResId, hedRes);
        return hedResId;
    }

    @Override
    public synchronized ResourceId<DownloadingRes> createDownloadingResource(ResourceId<HeadedRes> hedResId) {
        checkAlreadyExists(hedResId, DownloadingRes.class);
        HeadedRes newRes = (HeadedRes) data.get(hedResId);
        ResourceId<DownloadingRes> dingResId = new ResourceIdImpl<>(hedResId.getId());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dingRess.put(dingResId, os);
        DownloadingRes dingRes = new DownloadingResImpl(dingResId, newRes.getUrl(), os);
        data.remove(hedResId);
        data.put(dingRes.getId(), dingRes);
        log.debug("DownloadingRes is created: " + dingResId);
        return dingResId;
    }

    @Override
    @SneakyThrows
    public synchronized ResourceId<DownloadedRes> createDownloadedResource(ResourceId<DownloadingRes> dingResId) {
        checkAlreadyExists(dingResId, DownloadedRes.class);
        ResourceId<DownloadedRes> dedResId = new ResourceIdImpl<>(dingResId.getId());
        DownloadingRes dingRes = getResource(dingResId);
        byte[] bytes = dingRess.get(dingResId).toByteArray();
        DownloadedRes dedResource = new BytesDownloadedRes(dedResId, dingRes.getUrl(), bytes);
        data.remove(dingResId);
        data.put(dedResId, dedResource);
        log.debug("BytesDownloadedRes is created: " + dedResId);
        return dedResId;
    }

    @Override
    public synchronized <C> ResourceId<ParsingRes<C>> createParsingRes(ResourceId<DownloadedRes> dedResId) {
        checkAlreadyExists(dedResId, ParsingRes.class);
        ResourceId<ParsingRes<C>> pingResId = new ResourceIdImpl<>(dedResId.getId());
        DownloadedRes dedRes = (DownloadedRes) data.get(dedResId);
        ParsingRes<C> pingRes = new ParsingResImpl<>(pingResId, dedRes.getUrl(), dedRes.getContent());
        data.remove(dedResId);
        data.put(pingResId, pingRes);
        return pingRes.getId();
    }

    @Override
    @SneakyThrows
    public synchronized <C> ResourceId<ParsedRes<C>> createParsedRes(ResourceId<ParsingRes<C>> dedResId) {
        checkAlreadyExists(dedResId, ParsedRes.class);
        ResourceId<ParsedRes<C>> pedResId = new ResourceIdImpl<>(dedResId.getId());
        ParsingRes<C> pingRes = getResource(dedResId);
        ParsedRes<C> pedRes = new BytesParsedRes<>(pedResId, pingRes.getUrl(), pingRes.getParsedContent());
        data.remove(dedResId);
        data.put(pedResId, pedRes);
        return pedRes.getId();
    }

    @Override
    public <C> ResourceId<PackagingRes<C>> createPackagingRes(ResourceId<ParsedRes<C>> parsedResId) {
        checkAlreadyExists(parsedResId, PackagingRes.class);
        ResourceId<PackagingRes<C>> packagingResId = new ResourceIdImpl<>(parsedResId.getId());
        ParsedRes<C> parsedRes = getResource(parsedResId);
        PackagingRes<C> packagingRes = new PackagingResImpl<>(packagingResId, parsedRes.getUrl(), parsedRes.getParsedContent());
        data.remove(parsedResId);
        data.put(packagingResId, packagingRes);
        return packagingResId;
    }

    @Override
    public <C> ResourceId<PackagedRes> createPackagedRes(ResourceId<PackagingRes<C>> packagingResId) {
        checkAlreadyExists(packagingResId, PackagedRes.class);
        ResourceId<PackagedRes> packagedResId = new ResourceIdImpl<>(packagingResId.getId());
        PackagingRes<C> packagingRes = getResource(packagingResId);
        PackagedRes packagedRes = new PackagedResImpl(packagedResId, packagingRes.getUrl());
        data.remove(packagingResId);
        data.put(packagedResId, packagedRes);
        return packagedResId;
    }

    @Override
    public synchronized ResourceId<RejectedRes> createRejectedRes(ResourceId<?> resId) {
        checkAlreadyExists(resId, RejectedRes.class);
        ResourceId<RejectedRes> rejResId = new ResourceIdImpl<>(resId.getId());
        Resource res = data.get(resId);
        RejectedRes rejRes = new RejectedResImpl(rejResId, res.getUrl());
        data.remove(resId);
        data.put(rejResId, rejRes);
        log.debug("RejectedRes is created: " + rejRes.getId());
        return rejResId;
    }

    @Override
    public synchronized List<ResourceId<NewRes>> getNewResourceIds() {
        return data.values().stream()
                .filter(res -> res instanceof NewResImpl)
                .map(res -> (NewRes) res)
                .map(NewRes::getId)
                .collect(Collectors.toList());
    }

    private void checkAlreadyExists(ResourceId oldId, Class<? extends Resource> newResClass) {
        Resource oldRes = data.get(oldId);
        if (oldRes != null && comparator.isFirstGreaterOrEquals(oldRes.getClass(), newResClass)) {
            throw new ResourceAlreadyExistsException(oldId);
        }
    }
}
