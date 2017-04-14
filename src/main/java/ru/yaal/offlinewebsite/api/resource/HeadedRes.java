package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;

/**
 * @author Aleksey Yablokov
 */
public interface HeadedRes extends Resource<HeadedRes> {
    HttpInfo getHttpInfo();
}
