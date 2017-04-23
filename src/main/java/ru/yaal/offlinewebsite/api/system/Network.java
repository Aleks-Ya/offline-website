package ru.yaal.offlinewebsite.api.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.Link;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * @author Aleksey Yablokov
 */
public interface Network {
    InputStream openUrl(Link link) throws IOException;

    HttpInfo requestHttpInfo(Link link);
}
