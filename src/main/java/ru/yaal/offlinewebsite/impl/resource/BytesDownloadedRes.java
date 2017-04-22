package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@ToString
@EqualsAndHashCode
public class BytesDownloadedRes implements DownloadedRes {
    @Getter
    private final ResourceId<DownloadedRes> id;
    @Getter
    private final PageUrl url;
    private final byte[] bytes;
    @Getter
    private final HttpInfo httpInfo;

    public BytesDownloadedRes(ResourceId<DownloadedRes> id, PageUrl url, byte[] bytes, HttpInfo httpInfo) {
        this.id = id;
        this.url = url;
        this.bytes = bytes;
        this.httpInfo = httpInfo;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(bytes);
    }
}
