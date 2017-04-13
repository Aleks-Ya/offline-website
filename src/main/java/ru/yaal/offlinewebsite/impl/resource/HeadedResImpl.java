package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class HeadedResImpl<R extends HeadedRes.Id>
        extends AbstractResource<R>
        implements HeadedRes<R> {

    private final HttpInfo httpInfo;

    public HeadedResImpl(R id, SiteUrl url, HttpInfo httpInfo) {
        super(id, url);
        this.httpInfo = httpInfo;
    }
}
