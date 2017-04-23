package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.UuidLinkPackagerParams;
import ru.yaal.offlinewebsite.api.parser.UuidLink;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.link.LinkImpl;
import ru.yaal.offlinewebsite.impl.resource.ResIdImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceComparator;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;

/**
 * @author Aleksey Yablokov
 */
@ToString(of = {"priority", "outletDir"})
@Slf4j
public class UuidLinkPackager implements Packager {
    private final Path outletDir;
    private final OfflinePathResolver resolver;
    private final Storage storage;
    private final int priority;

    public UuidLinkPackager(UuidLinkPackagerParams params) {
        outletDir = params.getOutletDir();
        resolver = params.getOfflinePathResolver();
        storage = params.getStorage();
        priority = params.getPriority();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes> packingResId) {
        PackagingRes packingRes = storage.getResource(packingResId);
        log.debug("Pack resource (content type={}): {}", packingRes.getHttpInfo().getContentType(), packingRes);
        String contentStr = IOUtils.toString(packingRes.getContent(), Charset.defaultCharset());
        for (UuidLink link : packingRes.getLinks()) {
            String absoluteLink = link.getAbsolute();
            Resource res = storage.getResource(new ResIdImpl<>(absoluteLink));
            if (ResourceComparator.INSTANCE.isFirstGreaterOrEquals(res.getClass(), PackagingRes.class)) {
                String uuid = link.getUUID();
                if (!(res instanceof RejectedRes)) {
                    Path linkPath = resolver.internetUrlToOfflinePath(outletDir, new LinkImpl(absoluteLink));
                    String replacement = Matcher.quoteReplacement("file://" + linkPath.toString());
                    log.debug("Replace uuid {} with link {}", uuid, replacement);
                    contentStr = contentStr.replaceAll(uuid, replacement);
                } else {
                    contentStr = contentStr.replaceAll(uuid, absoluteLink);
                    log.debug("Stay absolute link {} to rejected resource: {}", absoluteLink, res);
                }
            } else {
                throw new IllegalStateException("Resource isn't downloaded: " + res);
            }
        }
        Path path = resolver.internetUrlToOfflinePath(outletDir, packingRes.getUrl());
        Path parentDir = path.getParent();
        if (Files.notExists(parentDir)) {
            Files.createDirectories(parentDir);
        }
        Files.createFile(path);
        IOUtils.write(contentStr, new FileOutputStream(path.toFile()), Charset.defaultCharset());
        ResourceId<PackagedRes> packagedResId = storage.createPackagedRes(packingResId, path);
        log.debug("Saved {} to {}", packagedResId, path);
        return packagedResId;
    }

    @Override
    public boolean accept(String contentType) {
        return HttpInfo.ContentTypes.HTML.equals(contentType);
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
