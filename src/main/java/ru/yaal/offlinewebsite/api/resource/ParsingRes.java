package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface ParsingRes<C, R extends ParsingRes.Id> extends Resource<R> {
    R getId();

    SiteUrl getUrl();

    InputStream getDownloadedContent();

    void setParsedContent(C content);

    C getParsedContent();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    class Id implements ResourceId {
        private final String id;
    }
}
