package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class UrlHelper {
    private UrlHelper() {
    }

    @SneakyThrows
    public static URL newAbsoluteURL(URL rootUrl, String relativeUrlStr) {
        return new URL(rootUrl, relativeUrlStr);
    }
}
