package ru.yaal.offlinewebsite.impl.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.params.HtmlPackagerParams;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * TODO split HtmlPackager to Replacer and Packager
 *
 * @author Aleksey Yablokov
 */
@Slf4j
public class HtmlPackager implements Packager<TagNode> {
    private final Path outletDir;
    private final OfflinePathResolver resolver;
    private final Storage storage;
    private final SimpleHtmlSerializer serializer = new SimpleHtmlSerializer(new CleanerProperties());
    private final List<Replacer<TagNode>> replacers;

    public HtmlPackager(HtmlPackagerParams params) {
        outletDir = params.getOutletDir();
        resolver = params.getOfflinePathResolver();
        storage = params.getStorage();
        replacers = params.getReplacers();
    }

    @Override
    @SneakyThrows
    public ResourceId<PackagedRes> pack(ResourceId<PackagingRes<TagNode>> packingResId) {
        PackagingRes<TagNode> packingRes = storage.getResource(packingResId);
        replacers.forEach(replacer -> replacer.replaceUrls(packingResId));
        Path path = resolver.internetUrlToOfflinePath(outletDir, packingRes.getUrl());
        Files.createDirectories(path.getParent());
        Files.createFile(path);
        serializer.writeToStream(packingRes.getContent(), new FileOutputStream(path.toFile()));
        ResourceId<PackagedRes> packagedResId = storage.createPackagedRes(packingResId, path);
        log.debug("Saved {} to {}", packagedResId, path);
        return packagedResId;
    }
}
