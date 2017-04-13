package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.Resource;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@EqualsAndHashCode
abstract class AbstractResource<R extends Resource.ResourceId> implements Resource<R> {
    @Getter
    private final R id;
    @Getter
    private final SiteUrl url;
}
