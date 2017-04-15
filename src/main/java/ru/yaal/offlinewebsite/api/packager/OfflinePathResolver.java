package ru.yaal.offlinewebsite.api.packager;

import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
public interface OfflinePathResolver {
    Path internetUrlToOfflinePath(Path outletDir, SiteUrl siteUrl);
}
