package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

/**
 * @author Aleksey Yablokov
 */
public interface PackageJobParams<C> extends Params {
    Storage getStorage();

    Packager<C> getPackager();

    ThreadPool getThreadPool();
}
