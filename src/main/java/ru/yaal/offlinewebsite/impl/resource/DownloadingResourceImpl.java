package ru.yaal.offlinewebsite.impl.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingResource;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class DownloadingResourceImpl<R extends DownloadingResourceImpl.Id> implements DownloadingResource<R> {
    private final R id;
    private final SiteUrl url;
    private final OutputStream outputStream;
}
