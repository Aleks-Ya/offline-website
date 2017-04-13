package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;

/**
 * @author Aleksey Yablokov
 */
public interface DownloaderParams extends Params {
    Storage getStorage();

    Network getNetwork();
}
