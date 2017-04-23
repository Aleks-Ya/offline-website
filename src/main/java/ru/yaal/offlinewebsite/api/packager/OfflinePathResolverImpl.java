package ru.yaal.offlinewebsite.api.packager;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.params.Link;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class OfflinePathResolverImpl implements OfflinePathResolver {
    @Override
    @SneakyThrows
    public Path internetUrlToOfflinePath(Path outletDir, Link link) {
        URL url = new URL(link.getOrigin());

        StringBuilder hostPortProtocol = new StringBuilder();
        hostPortProtocol.append(url.getHost());
        if (url.getPort() != -1) {
            hostPortProtocol.append("-");
            hostPortProtocol.append(url.getPort());
        }
        if (hostPortProtocol.length() > 0) {
            hostPortProtocol.append("-");
        }
        hostPortProtocol.append(url.getProtocol());

        String path = url.getPath().replace(":", "");

        String query = "";
        if (url.getQuery() != null) {
            query = URLEncoder.encode(url.getQuery(), Charset.defaultCharset().name());
        }

        log.debug("Resolve path: hostPortProtocol={}, path={}, query={}", hostPortProtocol.toString(), path, query);
        Path relPath = Paths.get(hostPortProtocol.toString(), path, query);
        return outletDir.resolve(relPath);
    }
}
