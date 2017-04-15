package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class ParsingResImpl<C> implements ParsingRes<C> {
    private final ResourceId<ParsingRes<C>> id;
    private final SiteUrl url;
    private final InputStream downloadedContent;

    @Setter
    private C parsedContent;
}
