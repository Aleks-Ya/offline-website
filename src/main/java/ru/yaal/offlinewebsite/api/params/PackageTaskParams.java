package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface PackageTaskParams extends Params {
    Storage getStorage();

    List<Packager> getPackagers();

    ResourceId<PackagingRes> getResource();
}
