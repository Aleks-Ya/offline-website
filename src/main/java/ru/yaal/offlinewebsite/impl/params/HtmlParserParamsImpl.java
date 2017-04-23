package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.HtmlParserParams;
import ru.yaal.offlinewebsite.api.params.Link;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class HtmlParserParamsImpl<C> implements HtmlParserParams<C> {
    private final Storage storage;
    private final Link link;
    private final List<UrlExtractor<C>> extractors;
    private final int priority;
}
