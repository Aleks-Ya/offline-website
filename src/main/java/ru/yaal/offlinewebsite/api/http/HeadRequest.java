package ru.yaal.offlinewebsite.api.http;

import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;

/**
 * @author Aleksey Yablokov
 */
public interface HeadRequest {
    HeadedRes.Id requestHead(HeadingRes.Id hingResId);
}
