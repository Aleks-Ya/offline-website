package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface ParsingRes extends Resource<ParsingRes> {

    InputStream getDownloadedContent();

    HttpInfo getHttpInfo();
}
