package ru.yaal.offlinewebsite.impl.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Yablokov
 */
public class BytesNetwork implements Network {
    private Map<ResUrl, byte[]> byteMap = new HashMap<>();
    private Map<ResUrl, HttpInfo> httpInfoMap = new HashMap<>();

    @Override
    public InputStream openUrl(ResUrl resUrl) {
        byte[] bytes = byteMap.get(resUrl);
        if (bytes != null) {
            return new ByteArrayInputStream(bytes);
        } else {
            throw new IllegalArgumentException("No byes for " + resUrl);
        }
    }

    @Override
    public HttpInfo requestHttpInfo(ResUrl resUrl) {
        HttpInfo httpInfo = httpInfoMap.get(resUrl);
        if (httpInfo != null) {
            return httpInfo;
        } else {
            throw new IllegalArgumentException("No HttpInfo for " + resUrl);
        }
    }

    public void putBytes(ResUrl resUrl, byte[] bytes) {
        byteMap.put(resUrl, bytes);
    }

    public void putBytes(ResUrl resUrl, String content) {
        putBytes(resUrl, content.getBytes());
    }

    public void putHttpInfo(ResUrl resUrl, HttpInfo httpInfo) {
        httpInfoMap.put(resUrl, httpInfo);
    }
}
