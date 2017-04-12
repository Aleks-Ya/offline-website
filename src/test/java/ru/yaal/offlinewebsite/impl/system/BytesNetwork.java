package ru.yaal.offlinewebsite.impl.system;

import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public class BytesNetwork implements Network {
    private final byte[] bytes;

    public BytesNetwork(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public InputStream openUrl(SiteUrl url) {
        return new ByteArrayInputStream(bytes);
    }
}
