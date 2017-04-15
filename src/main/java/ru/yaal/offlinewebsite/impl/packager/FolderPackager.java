package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackagerParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.FileOutputStream;
import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public class FolderPackager implements Packager<TagNode> {
    private final Path outletDir;
    private final OfflinePathResolver resolver;
    private final Storage storage;
    private final SimpleHtmlSerializer serializer = new SimpleHtmlSerializer(new CleanerProperties());

    public FolderPackager(PackagerParams params) {
        outletDir = params.getOutletDir();
        resolver = params.getOfflinePathResolver();
        storage = params.getStorage();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes<TagNode>> packingResId) {
        PackagingRes<TagNode> packingRes = storage.getResource(packingResId);
        Path path = resolver.internetUrlToOfflinePath(outletDir, packingRes.getUrl());
        serializer.writeToStream(packingRes.getContent(), new FileOutputStream(path.toFile()));
        return storage.createPackagedRes(packingResId);
    }
}
