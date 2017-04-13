package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.HttpHeadingResource;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class HeadingResImpl<R extends HttpHeadingResource.Id> implements HttpHeadingResource<R> {
    private final R id;
    private final SiteUrl url;
}
