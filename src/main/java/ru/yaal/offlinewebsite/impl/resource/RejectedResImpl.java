package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
public class RejectedResImpl
        extends AbstractRes<RejectedRes>
        implements RejectedRes {

    public RejectedResImpl(ResourceId<RejectedRes> id, SiteUrl url) {
        super(id, url);
    }
}
