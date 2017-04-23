package ru.yaal.offlinewebsite.api.packager;

import ru.yaal.offlinewebsite.api.link.Link;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public interface OfflinePathResolver {
    Path internetUrlToOfflinePath(Path outletDir, Link link);
}
