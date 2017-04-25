package ru.yaal.offlinewebsite.api.packager;

import ru.yaal.offlinewebsite.api.params.ResUrl;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public interface OfflinePathResolver {
    Path internetUrlToOfflinePath(Path outletDir, ResUrl resUrl);
}
