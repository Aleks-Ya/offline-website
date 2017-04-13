package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
public class BytesParsedRes<R extends ParsedRes.Id>
        extends AbstractResource<R>
        implements ParsedRes<R> {
    private final byte[] bytes;

    public BytesParsedRes(R id, SiteUrl url, byte[] bytes) {
        super(id, url);
        this.bytes = bytes;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(bytes);
    }
}
