package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.parser.UuidAbsoluteLink;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
public interface PackagingRes extends Resource<PackagingRes> {
    HttpInfo getHttpInfo();

    InputStream getContent();

    List<UuidAbsoluteLink> getLinks();
}
