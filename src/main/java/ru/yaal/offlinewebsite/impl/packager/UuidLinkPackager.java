package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.InputStreamPackagerParams;
import ru.yaal.offlinewebsite.api.parser.UuidAbsoluteLink;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;
import ru.yaal.offlinewebsite.impl.resource.ResourceIdImpl;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO split CopyPackager to Replacer and Packager
 *
 * @author Aleksey Yablokov
 */
@Slf4j
public class UuidLinkPackager implements Packager {
    private final Path outletDir;
    private final OfflinePathResolver resolver;
    private final Storage storage;

    public UuidLinkPackager(InputStreamPackagerParams params) {
        outletDir = params.getOutletDir();
        resolver = params.getOfflinePathResolver();
        storage = params.getStorage();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes> packingResId) {
        PackagingRes packagingRes = storage.getResource(packingResId);
        String contentStr = IOUtils.toString(packagingRes.getContent(), Charset.defaultCharset());
        for (UuidAbsoluteLink link : packagingRes.getLinks()) {
            Resource res = storage.getResource(new ResourceIdImpl<>(link.getAbsolute()));
            if (res instanceof PackagedRes) {
                Path linkPath = resolver.internetUrlToOfflinePath(outletDir, new SiteUrlImpl(link.getAbsolute()));
                contentStr = contentStr.replaceAll(link.getUUID(), linkPath.toString());
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
