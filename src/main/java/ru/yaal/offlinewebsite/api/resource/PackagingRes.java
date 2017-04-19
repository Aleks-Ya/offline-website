package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;

/**
 * @author Aleksey Yablokov
 */
public interface PackagingRes<C> extends Resource<PackagingRes<C>> {
    HttpInfo getHttpInfo();

    C getContent();
}
