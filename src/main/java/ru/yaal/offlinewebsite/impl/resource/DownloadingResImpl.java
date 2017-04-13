package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DownloadingResImpl<R extends DownloadingResImpl.Id>
        extends AbstractResource<R>
        implements DownloadingRes<R> {
    private final OutputStream outputStream;

    public DownloadingResImpl(R id, SiteUrl url, OutputStream outputStream) {
        super(id, url);
        this.outputStream = outputStream;
    }
}
