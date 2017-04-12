package ru.yaal.offlinewebsite.api.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class DownloadingResource<R extends DownloadingResource.Id> implements Resource<R> {
    private final R id;
    private final OutputStream outputStream;

    OutputStream getOutputStream() {
        return outputStream;
    }

    @RequiredArgsConstructor
    @Getter
    public static class Id implements ResourceId {
        private final String id;
    }
}
