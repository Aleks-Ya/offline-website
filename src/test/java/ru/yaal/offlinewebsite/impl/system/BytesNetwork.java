package ru.yaal.offlinewebsite.impl.system;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public class BytesNetwork implements Network {
    private final byte[] bytes;
    private final HttpInfo httpInfo;

    public BytesNetwork(byte[] bytes, HttpInfo httpInfo) {
        this.bytes = bytes;
        this.httpInfo = httpInfo;
    }

    public BytesNetwork(byte[] bytes) {
        this.bytes = bytes;
        this.httpInfo = null;
    }

    @Override
    public InputStream openUrl(SiteUrl url) {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public HttpInfo requestHttpInfo(SiteUrl url) {
        return httpInfo;
    }
}
