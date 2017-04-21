package ru.yaal.offlinewebsite.api.http;

import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface HeadRetriever {
    ResourceId<HeadedRes> requestHead(ResourceId<HeadingRes> hingResId);
}
