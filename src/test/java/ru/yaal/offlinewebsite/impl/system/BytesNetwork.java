package ru.yaal.offlinewebsite.impl.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class BytesNetwork implements Network {
    private Map<SiteUrl, byte[]> byteMap = new HashMap<>();
    private Map<SiteUrl, HttpInfo> httpInfoMap = new HashMap<>();

    @Override
    public InputStream openUrl(SiteUrl siteUrl) {
        byte[] bytes = byteMap.get(siteUrl);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        } else {
            throw new IllegalArgumentException("No byes for " + siteUrl);
        }
    }

    @Override
    public HttpInfo requestHttpInfo(SiteUrl siteUrl) {
        HttpInfo httpInfo = httpInfoMap.get(siteUrl);
        if (httpInfo != null) {
            return httpInfo;
        } else {
            throw new IllegalArgumentException("No HttpInfo for " + siteUrl);
        }
    }

    public void putBytes(SiteUrl siteUrl, byte[] bytes) {
        byteMap.put(siteUrl, bytes);
    }

    public void putBytes(SiteUrl siteUrl, String content) {
        putBytes(siteUrl, content.getBytes());
    }

    public void putHttpInfo(SiteUrl siteUrl, HttpInfo httpInfo) {
        httpInfoMap.put(siteUrl, httpInfo);
    }
}
