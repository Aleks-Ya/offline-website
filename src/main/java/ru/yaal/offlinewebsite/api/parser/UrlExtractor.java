package ru.yaal.offlinewebsite.api.parser;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface UrlExtractor<C> {
    List<UuidLink> extract(C content);
}
