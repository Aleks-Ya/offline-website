package ru.yaal.offlinewebsite.api.packager;

import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface Packager<C> {
    ResourceId<PackagedRes> pack(ResourceId<PackagingRes<C>> pickResId);
}
