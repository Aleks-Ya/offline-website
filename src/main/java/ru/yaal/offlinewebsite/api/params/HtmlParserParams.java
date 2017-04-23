package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.link.Link;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface HtmlParserParams<C> extends Params {
    Storage getStorage();

    Link getLink();

    List<UrlExtractor<C>> getExtractors();

    int getPriority();
}
