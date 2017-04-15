package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.params.PackageJobParams;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class PackageJobParamsImpl<C> implements PackageJobParams<C> {
    private final Storage storage;
    private final Packager<C> packager;
    private final ThreadPool threadPool;
}
