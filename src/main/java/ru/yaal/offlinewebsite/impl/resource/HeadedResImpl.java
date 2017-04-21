package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class HeadedResImpl extends AbstractRes<HeadedRes> implements HeadedRes {

    private final HttpInfo httpInfo;

    public HeadedResImpl(ResourceId<HeadedRes> id, PageUrl url, HttpInfo httpInfo) {
        super(id, url);
        this.httpInfo = httpInfo;
    }
}
