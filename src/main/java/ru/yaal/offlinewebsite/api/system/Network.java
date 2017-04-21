package ru.yaal.offlinewebsite.api.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface Network {
    InputStream openUrl(PageUrl pageUrl);

    HttpInfo requestHttpInfo(PageUrl pageUrl);
}
