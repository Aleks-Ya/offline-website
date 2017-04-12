package ru.yaal.offlinewebsite.api.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class DownloadedResource<R extends DownloadedResource.Id> implements Resource<R> {
    private final R id;

    @RequiredArgsConstructor
    @Getter
    public static class Id implements ResourceId {
        private final String id;
    }
}
