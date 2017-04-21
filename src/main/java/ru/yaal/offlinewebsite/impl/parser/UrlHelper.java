package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
class UrlHelper {
    private UrlHelper() {
    }

    @SneakyThrows
    static String toAbsoluteUrlStr(String pageUrl, String relativeUrlStr) {
        URL rootUrl = new URL(pageUrl);
        return new URL(rootUrl, relativeUrlStr).toString();
    }
}
