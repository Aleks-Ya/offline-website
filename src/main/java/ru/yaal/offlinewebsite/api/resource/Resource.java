package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.link.Link;

/**
 * @author Aleksey Yablokov
 */
public interface Resource<R extends Resource<?>> {
    ResourceId<R> getId();

    Link getUrl();
}
