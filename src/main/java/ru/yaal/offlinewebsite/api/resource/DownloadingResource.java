package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadingResource<R extends DownloadingResource.Id> extends Resource<R> {
    R getId();

    SiteUrl getUrl();

    java.io.OutputStream getOutputStream();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    class Id implements ResourceId {
        private final String id;
    }
}
