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

        StringBuilder hostPortProtocol = new StringBuilder();
        hostPortProtocol.append(url.getHost());
        if (url.getPort() != -1) {
            hostPortProtocol.append("-");
            hostPortProtocol.append(url.getPort());
        }
        hostPortProtocol.append("-");
        hostPortProtocol.append(url.getProtocol());

        String path = url.getPath();

        String query = "";
        if (url.getQuery() != null) {
            query = URLEncoder.encode(url.getQuery(), Charset.defaultCharset().name());
        }

        Path relPath = Paths.get(hostPortProtocol.toString(), path, query);
        return outletDir.resolve(relPath);
    }
}
