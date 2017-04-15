package ru.yaal.offlinewebsite.api.packager;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Aleksey Yablokov
 */
public class OfflinePathResolverImpl implements OfflinePathResolver {
    @Override
    @SneakyThrows
    public Path internetUrlToOfflinePath(Path outletDir, SiteUrl siteUrl) {
        URL url = new URL(siteUrl.getUrl());
        String protocol = url.getProtocol();
        String port = String.valueOf(url.getPort());
        String host = url.getHost();
        String path = url.getPath();
        String query = URLEncoder.encode(url.getQuery(), Charset.defaultCharset().name());

        Path relPath = Paths.get(host + "-" + port + "-" + protocol, path, query);
        return outletDir.resolve(relPath);
    }
}
