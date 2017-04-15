package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface ParsingRes<C> extends Resource<ParsingRes<C>> {

    InputStream getDownloadedContent();

    void setParsedContent(C content);

    C getParsedContent();

    HttpInfo getHttpInfo();
}
