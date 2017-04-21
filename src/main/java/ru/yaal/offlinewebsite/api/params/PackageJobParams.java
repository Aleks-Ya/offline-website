package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.packager.Packager;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface PackageJobParams extends Params {
    Storage getStorage();

    List<Packager> getPackagers();

    ThreadPool getThreadPool();
}
