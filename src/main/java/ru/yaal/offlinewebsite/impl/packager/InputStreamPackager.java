package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.InputStreamPackagerParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TODO split HtmlPackager to Replacer and Packager
 *
 * @author Aleksey Yablokov
 */
@Slf4j
public class InputStreamPackager implements Packager<InputStream> {
    private final Path outletDir;
    private final OfflinePathResolver resolver;
    private final Storage storage;

    public InputStreamPackager(InputStreamPackagerParams params) {
        outletDir = params.getOutletDir();
        resolver = params.getOfflinePathResolver();
        storage = params.getStorage();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes<InputStream>> packingResId) {
        PackagingRes<InputStream> packingRes = storage.getResource(packingResId);
        Path path = resolver.internetUrlToOfflinePath(outletDir, packingRes.getUrl());
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        IOUtils.copy(packingRes.getContent(), new FileOutputStream(path.toFile()));
        ResourceId<PackagedRes> packagedResId = storage.createPackagedRes(packingResId, path);
        log.debug("Saved {} to {}", packagedResId, path);
        return packagedResId;
    }
}
