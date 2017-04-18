package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface ParserParams<C> extends Params {
    Storage getStorage();

    SiteUrl getRootSiteUrl();

    List<UrlExtractor<C>> getExtractors();

    int getPriority();
}
