package ru.yaal.offlinewebsite.api.resource;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface ParsingRes<C> extends Resource<ParsingRes> {

    InputStream getDownloadedContent();

    void setParsedContent(C content);

    C getParsedContent();
}
