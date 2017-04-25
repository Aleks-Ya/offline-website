package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class HeadedResImpl implements HeadedRes {
    private final ResourceId<HeadedRes> id;
    private final ResUrl url;
    private final HttpInfo httpInfo;
}
