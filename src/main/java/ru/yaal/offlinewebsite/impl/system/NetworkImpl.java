package ru.yaal.offlinewebsite.impl.system;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.system.Network;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class NetworkImpl implements Network {
    @Override
    @SneakyThrows
    public InputStream openUrl(SiteUrl url) {
        return new URL(url.getUrl()).openStream();
    }
}
