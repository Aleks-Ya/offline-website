package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface PackageTaskParams extends Params {
    Storage getStorage();

    Packager getHtmlPackager();

    Packager getInputStreamPackager();

    ResourceId<PackagingRes> getResource();
}
