package ru.yaal.offlinewebsite.impl.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.resource.DownloadedResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
public class BytesDownloadedResource<R extends BytesDownloadedResource.Id> implements DownloadedResource<R> {
    @Getter
    private final R id;
    private final byte[] bytes;

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(bytes);
    }
}
