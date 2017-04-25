package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.params.ResUrl;

/**
 * @author Aleksey Yablokov
 */
public interface Resource<R extends Resource<?>> {
    ResourceId<R> getId();

    ResUrl getUrl();
}
