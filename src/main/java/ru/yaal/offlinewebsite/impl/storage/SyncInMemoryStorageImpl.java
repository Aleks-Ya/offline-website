package ru.yaal.offlinewebsite.impl.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.*;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.resource.*;

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
    private final Map<DownloadingRes.Id, ByteArrayOutputStream> dingRess = new HashMap<>();

    @Override
    public synchronized <ID extends ResourceId, R extends Resource<ID>> R getResource(ID id) {
        return (R) data.get(id);
    }

    @Override
    public <ID extends ResourceId> boolean hasResource(ID id) {
        return getResource(id) != null;
    }

    @Override
    public synchronized NewRes.Id createNewResource(SiteUrl url) {
        NewRes.Id newResId = new NewRes.Id(url.getUrl());
        checkAlreadyExists(newResId);
        NewResImpl newRes = new NewResImpl<>(newResId, url);
        data.put(newRes.getId(), newRes);
        log.debug("NewRes is created: " + newResId);
        return newResId;
    }

    @Override
    public synchronized HeadingRes.Id createHeadingResource(NewRes.Id newResId) {
        HeadingRes.Id hingResId = new HeadingRes.Id(newResId.getId());
        checkAlreadyExists(hingResId);
        NewRes newRes = (NewRes) data.get(newResId);
        HeadingRes hingRes = new HeadingResImpl<>(hingResId, newRes.getUrl());
        data.remove(newResId);
        data.put(hingResId, hingRes);
        return hingResId;
    }

    @Override
    public synchronized HeadedRes.Id createHeadedResource(HeadingRes.Id hingResId, HttpInfo httpInfo) {
        HeadedRes.Id hedResId = new HeadedRes.Id(hingResId.getId());
        checkAlreadyExists(hedResId);
        HeadingRes hingRes = (HeadingRes) data.get(hingResId);
        HeadedRes hedRes = new HeadedResImpl<>(hedResId, hingRes.getUrl(), httpInfo);
        data.remove(hingResId);
        data.put(hedResId, hedRes);
        return hedResId;
    }

    @Override
    public synchronized DownloadingRes.Id createDownloadingResource(HeadedRes.Id hedResId) {
        HeadedRes newRes = (HeadedRes) data.get(hedResId);
        DownloadingRes.Id dingResId = new DownloadingRes.Id(hedResId.getId());
        checkAlreadyExists(dingResId);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        dingRess.put(dingResId, os);
        DownloadingRes dingRes = new DownloadingResImpl<>(dingResId, newRes.getUrl(), os);
        data.remove(hedResId);
        data.put(dingRes.getId(), dingRes);
        log.debug("DownloadingRes is created: " + dingResId);
        return dingResId;
    }

    @Override
    @SneakyThrows
    public synchronized DownloadedRes.Id createDownloadedResource(DownloadingRes.Id dingResId) {
        DownloadedRes.Id dedResId = new BytesDownloadedRes.Id(dingResId.getId());
        checkAlreadyExists(dedResId);
        DownloadingRes<DownloadingRes.Id> dingRes = getResource(dingResId);
        byte[] bytes = dingRess.get(dingResId).toByteArray();
        DownloadedRes dedResource = new BytesDownloadedRes<>(dedResId, dingRes.getUrl(), bytes);
        data.remove(dingResId);
        data.put(dedResId, dedResource);
        log.debug("BytesDownloadedRes is created: " + dedResId);
        return dedResId;
    }

    @Override
    public synchronized ParsingRes.Id createParsingRes(DownloadedRes.Id dedResId) {
        ParsingRes.Id pingResId = new ParsingRes.Id(dedResId.getId());
        checkAlreadyExists(pingResId);
        DownloadedRes dedRes = (DownloadedRes) data.get(dedResId);
        ParsingRes pingRes = new ParsingResImpl<>(pingResId, dedRes.getUrl(), dedRes.getContent());
        data.remove(dedResId);
        data.put(pingResId, pingRes);
        return pingResId;
    }

    @Override
    @SneakyThrows
    public synchronized ParsedRes.Id createParsedRes(ParsingRes.Id dedResId) {
        ParsedRes.Id pedResId = new ParsedRes.Id(dedResId.getId());
        checkAlreadyExists(pedResId);
        ParsingRes<TagNode, ParsingRes.Id> pingRes = (ParsingRes<TagNode, ParsingRes.Id>) data.get(dedResId);
        ParsedRes<TagNode, ParsedRes.Id> pedRes = new BytesParsedRes<>(pedResId, pingRes.getUrl(), pingRes.getParsedContent());
        data.remove(dedResId);
        data.put(pedResId, pedRes);
        return pedResId;
    }

    @Override
    public synchronized RejectedRes.Id createRejectedRes(ResourceId resId) {
        RejectedRes.Id rejResId = new RejectedRes.Id(resId.getId());
        checkAlreadyExists(rejResId);
        Resource res = data.get(resId);
        RejectedRes<RejectedRes.Id> rejRes = new RejectedResImpl<>(rejResId, res.getUrl());
        data.remove(resId);
        data.put(rejResId, rejRes);
        log.debug("RejectedRes is created: " + rejRes.getId());
        return rejResId;
    }

    @Override
    public synchronized List<NewRes.Id> getNewResourceIds() {
        return data.values().stream()
                .filter(res -> res instanceof NewResImpl)
                .map(res -> (NewRes) res)
                .map(NewRes::getId)
                .collect(Collectors.toList());
    }

    private void checkAlreadyExists(ResourceId id) {
        if (data.containsKey(id)) {
            throw new ResourceAlreadyExistsException(id);
        }
    }
}
