package ru.yaal.offlinewebsite.impl.system;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class NetworkImpl implements Network {

    @Override
    @SneakyThrows
    public InputStream openUrl(PageUrl pageUrl) {
        return new URL(pageUrl.getUrl()).openStream();
    }

    @Override
    @SneakyThrows
    public HttpInfo requestHttpInfo(PageUrl pageUrl) {
        URL url = new URL(pageUrl.getUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        conn.connect();
        int responseCode = conn.getResponseCode();
        int contentLength = conn.getContentLength();
        long lastModified = conn.getLastModified();
        String contentType = conn.getContentType();
        conn.disconnect();
        return new HttpInfoImpl(responseCode, contentLength, lastModified, contentType);
    }
}
