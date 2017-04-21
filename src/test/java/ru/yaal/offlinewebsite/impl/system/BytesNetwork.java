package ru.yaal.offlinewebsite.impl.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class BytesNetwork implements Network {
    private Map<PageUrl, byte[]> byteMap = new HashMap<>();
    private Map<PageUrl, HttpInfo> httpInfoMap = new HashMap<>();

    @Override
    public InputStream openUrl(PageUrl pageUrl) {
        byte[] bytes = byteMap.get(pageUrl);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        } else {
            throw new IllegalArgumentException("No byes for " + pageUrl);
        }
    }

    @Override
    public HttpInfo requestHttpInfo(PageUrl pageUrl) {
        HttpInfo httpInfo = httpInfoMap.get(pageUrl);
        if (httpInfo != null) {
            return httpInfo;
        } else {
            throw new IllegalArgumentException("No HttpInfo for " + pageUrl);
        }
    }

    public void putBytes(PageUrl pageUrl, byte[] bytes) {
        byteMap.put(pageUrl, bytes);
    }

    public void putBytes(PageUrl pageUrl, String content) {
        putBytes(pageUrl, content.getBytes());
    }

    public void putHttpInfo(PageUrl pageUrl, HttpInfo httpInfo) {
        httpInfoMap.put(pageUrl, httpInfo);
    }
}
