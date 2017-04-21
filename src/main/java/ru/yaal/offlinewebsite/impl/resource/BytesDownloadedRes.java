package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;
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
    @Getter
    private final HttpInfo httpInfo;

    public BytesDownloadedRes(ResourceId<DownloadedRes> id, PageUrl url, byte[] bytes, HttpInfo httpInfo) {
        super(id, url);
        this.bytes = bytes;
        this.httpInfo = httpInfo;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(bytes);
    }
}
