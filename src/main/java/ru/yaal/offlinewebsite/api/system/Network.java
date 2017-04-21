package ru.yaal.offlinewebsite.api.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * @author Aleksey Yablokov
 */
public interface Network {
    InputStream openUrl(PageUrl pageUrl) throws IOException;

    HttpInfo requestHttpInfo(PageUrl pageUrl);
}
