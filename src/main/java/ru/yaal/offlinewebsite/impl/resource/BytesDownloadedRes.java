package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
public class BytesDownloadedRes extends AbstractRes<DownloadedRes> implements DownloadedRes {
    private final byte[] bytes;

    public BytesDownloadedRes(ResourceId<DownloadedRes> id, SiteUrl url, byte[] bytes) {
        super(id, url);
        this.bytes = bytes;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(bytes);
    }
}
