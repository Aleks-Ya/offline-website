package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.CopyPackagerParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class CopyPackager implements Packager {
    private final Path outletDir;
    private final Storage storage;
    private final OfflinePathResolver resolver;

    public CopyPackager(CopyPackagerParams params) {
        outletDir = params.getOutletDir();
        storage = params.getStorage();
        resolver = params.getOfflinePathResolver();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes> packingResId) {
        PackagingRes packingRes = storage.getResource(packingResId);
        Path path = resolver.internetUrlToOfflinePath(outletDir, packingRes.getUrl());
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        IOUtils.copy(packingRes.getContent(), new FileOutputStream(path.toFile()));
        ResourceId<PackagedRes> packagedResId = storage.createPackagedRes(packingResId, path);
        log.debug("Saved {} to {}", packagedResId, path);
        return packagedResId;
    }
}
