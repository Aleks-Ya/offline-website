package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadingRes<R extends DownloadingRes.Id> extends Resource<R> {
    R getId();

    SiteUrl getUrl();

    OutputStream getOutputStream();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    class Id implements ResourceId {
        private final String id;
    }
}
