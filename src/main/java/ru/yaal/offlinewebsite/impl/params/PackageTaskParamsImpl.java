package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageTaskParams;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class PackageTaskParamsImpl<C> implements PackageTaskParams<C> {
    private final Storage storage;
    private final Packager<C> packager;
    private final ResourceId<PackagingRes<C>> resource;
}
