package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
public interface ParsingRes<R extends ParsingRes.Id> extends Resource<R> {
    R getId();

    SiteUrl getUrl();

    InputStream getDownloadedContent();

    OutputStream getParsedContentOutputStream();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    class Id implements ResourceId {
        private final String id;
    }
}
