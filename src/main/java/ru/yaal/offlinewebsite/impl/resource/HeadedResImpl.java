package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.HttpHeadedResource;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class HeadedResImpl<R extends HttpHeadedResource.Id> implements HttpHeadedResource<R> {
    private final R id;
    private final SiteUrl url;
    private final HttpInfo httpInfo;
}
