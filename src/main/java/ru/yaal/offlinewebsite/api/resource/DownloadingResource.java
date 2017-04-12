package ru.yaal.offlinewebsite.api.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadingResource<R extends DownloadingResource.Id> extends Resource<R> {
    R getId();

    ru.yaal.offlinewebsite.api.SiteUrl getUrl();

    java.io.OutputStream getOutputStream();

    @RequiredArgsConstructor
    @Getter
    class Id implements ResourceId {
        private final String id;
    }
}
