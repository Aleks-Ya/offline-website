package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.api.thread.ThreadPool;

/**
 * @author Aleksey Yablokov
 */
public interface HeadRequestParams extends Params {
    Storage getStorage();

    Network getNetwork();
}
