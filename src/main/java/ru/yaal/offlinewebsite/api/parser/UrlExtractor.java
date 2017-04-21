package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.params.PageUrl;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface UrlExtractor<C> {
    List<UuidLink> extract(C content, PageUrl pageUrl);
}
