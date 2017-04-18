package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class ParserParamsImpl<C> implements ParserParams<C> {
    private final Storage storage;
    private final SiteUrl rootSiteUrl;
    private final List<UrlExtractor<C>> extractors;
    private final int priority;
}
