package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadedResource<R extends DownloadedResource.Id> extends Resource<R> {
    R getId();

    InputStream getContent();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    class Id implements ResourceId {
        private final String id;
    }
}
