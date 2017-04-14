package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class DownloadingResImpl extends AbstractRes<DownloadingRes> implements DownloadingRes {

    private final OutputStream outputStream;

    public DownloadingResImpl(ResourceId<DownloadingRes> id, SiteUrl url, OutputStream outputStream) {
        super(id, url);
        this.outputStream = outputStream;
    }
}
