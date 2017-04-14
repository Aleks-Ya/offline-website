package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class HeadingResImpl extends AbstractRes<HeadingRes> implements HeadingRes {
    public HeadingResImpl(ResourceId<HeadingRes> id, SiteUrl url) {
        super(id, url);
    }
}
