package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;

/**
 * @author Aleksey Yablokov
 */
public interface ParsedRes<C> extends Resource<ParsedRes<C>> {
    C getParsedContent();

    HttpInfo getHttpInfo();
}
