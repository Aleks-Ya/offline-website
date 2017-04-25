package ru.yaal.offlinewebsite.impl.system;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.system.Network;
import ru.yaal.offlinewebsite.impl.http.HttpInfoImpl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class NetworkImpl implements Network {

    @Override
    public InputStream openUrl(ResUrl resUrl) throws IOException {
        return new URL(resUrl.getUrl()).openStream();
    }

    @Override
    @SneakyThrows
    public HttpInfo requestHttpInfo(ResUrl resUrl) {
        URL url = new URL(resUrl.getUrl());
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
