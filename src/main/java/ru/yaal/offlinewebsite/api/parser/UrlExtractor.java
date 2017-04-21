package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface UrlExtractor<C> {
    List<UuidLink> extract(C content, SiteUrl siteUrl);
}
