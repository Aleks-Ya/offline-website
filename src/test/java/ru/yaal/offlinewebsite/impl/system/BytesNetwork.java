package ru.yaal.offlinewebsite.impl.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.link.Link;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class BytesNetwork implements Network {
    private Map<Link, byte[]> byteMap = new HashMap<>();
    private Map<Link, HttpInfo> httpInfoMap = new HashMap<>();

    @Override
    public InputStream openUrl(Link link) {
        byte[] bytes = byteMap.get(link);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        } else {
            throw new IllegalArgumentException("No byes for " + link);
        }
    }

    @Override
    public HttpInfo requestHttpInfo(Link link) {
        HttpInfo httpInfo = httpInfoMap.get(link);
        if (httpInfo != null) {
            return httpInfo;
        } else {
            throw new IllegalArgumentException("No HttpInfo for " + link);
        }
    }

    public void putBytes(Link link, byte[] bytes) {
        byteMap.put(link, bytes);
    }

    public void putBytes(Link link, String content) {
        putBytes(link, content.getBytes());
    }

    public void putHttpInfo(Link link, HttpInfo httpInfo) {
        httpInfoMap.put(link, httpInfo);
    }
}
