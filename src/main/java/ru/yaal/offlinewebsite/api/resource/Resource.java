package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.params.SiteUrl;

/**
 * @author Aleksey Yablokov
 */
public interface Resource<R extends Resource<?>> {
    ResourceId<R> getId();

    SiteUrl getUrl();
}
