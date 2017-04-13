package ru.yaal.offlinewebsite.api.http;

import ru.yaal.offlinewebsite.api.resource.HttpHeadedResource;
import ru.yaal.offlinewebsite.api.resource.HttpHeadingResource;

/**
 * @author Aleksey Yablokov
 */
public interface HeadRequest {
    HttpHeadedResource.Id requestHead(HttpHeadingResource.Id hingResId);
}
