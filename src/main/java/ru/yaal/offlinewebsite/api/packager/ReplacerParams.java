package ru.yaal.offlinewebsite.api.packager;

import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface ReplacerParams {
    Storage getStorage();

    SiteUrl getRootSiteUrl();
}
