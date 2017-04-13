package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class ParsingResImpl<R extends ParsingRes.Id> implements ParsingRes<R> {
    private final R id;
    private final SiteUrl url;
    private final InputStream downloadedContent;
    private final OutputStream parsedContentOutputStream;
}
