package ru.yaal.offlinewebsite.api.packager;

import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface Replacer<C> {
    ResourceId<PackagingRes<C>> replaceUrls(ResourceId<PackagingRes<C>> packagingResId);
}
