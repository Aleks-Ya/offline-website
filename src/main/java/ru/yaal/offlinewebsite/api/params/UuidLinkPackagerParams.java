package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.packager.OfflinePathResolver;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public interface UuidLinkPackagerParams extends Params {
    Path getOutletDir();

    OfflinePathResolver getOfflinePathResolver();

    Storage getStorage();

    int getPriority();
}
