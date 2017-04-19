package ru.yaal.offlinewebsite.api.params;

import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.packager.Replacer;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface PackageTaskParams extends Params {
    Storage getStorage();

    Packager<TagNode> getHtmlPackager();

    Packager<InputStream> getInputStreamPackager();

    ResourceId<PackagingRes<Object>> getResource();

    List<Replacer<TagNode>> getReplacers();
}
