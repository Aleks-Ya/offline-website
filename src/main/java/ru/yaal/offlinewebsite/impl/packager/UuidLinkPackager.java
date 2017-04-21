package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.UuidLinkPackagerParams;
import ru.yaal.offlinewebsite.api.parser.UuidLink;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.PageUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class UuidLinkPackager implements Packager {
    private final Path outletDir;
    private final OfflinePathResolver resolver;
    private final Storage storage;

    public UuidLinkPackager(UuidLinkPackagerParams params) {
        outletDir = params.getOutletDir();
        resolver = params.getOfflinePathResolver();
        storage = params.getStorage();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes> packingResId) {
        PackagingRes packagingRes = storage.getResource(packingResId);
        String contentStr = IOUtils.toString(packagingRes.getContent(), Charset.defaultCharset());
        for (UuidLink link : packagingRes.getLinks()) {
            Resource res = storage.getResource(new ResourceIdImpl<>(link.getAbsolute()));
            if (res instanceof PackagedRes) {
                Path linkPath = resolver.internetUrlToOfflinePath(outletDir, new PageUrlImpl(link.getAbsolute()));
                String uuid = link.getUUID();
                String replacement = linkPath.toString();
                log.debug("Replace uuid {} with link {}", uuid, replacement);
                contentStr = contentStr.replaceAll(uuid, replacement);
            }
        }
        Path path = resolver.internetUrlToOfflinePath(outletDir, packagingRes.getUrl());
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        IOUtils.write(contentStr, new FileOutputStream(path.toFile()), Charset.defaultCharset());
        ResourceId<PackagedRes> packagedResId = storage.createPackagedRes(packingResId, path);
        log.debug("Saved {} to {}", packagedResId, path);
        return packagedResId;
    }
}
