package ru.yaal.offlinewebsite.api.system;

import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface Network {
    InputStream openUrl(SiteUrl url);
}
