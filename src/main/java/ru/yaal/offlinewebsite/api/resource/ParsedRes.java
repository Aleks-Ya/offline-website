package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.parser.UuidLink;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface ParsedRes extends Resource<ParsedRes> {
    InputStream getParsedContent();

    HttpInfo getHttpInfo();

    List<UuidLink> getLinks();
}
