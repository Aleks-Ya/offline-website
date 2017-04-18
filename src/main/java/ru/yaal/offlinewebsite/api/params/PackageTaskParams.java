package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface PackageTaskParams<C> extends Params {
    Packager<C> getPackager();

    ResourceId<PackagingRes<C>> getResource();
}
