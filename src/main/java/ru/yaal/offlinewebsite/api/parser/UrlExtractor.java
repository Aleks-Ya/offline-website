package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.params.Link;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface UrlExtractor<C> {
    List<UuidLink> extract(C content, Link link);
}
