package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface ParserParams extends Params {
    Storage getStorage();
    SiteUrl getRootSiteUrl();
}
