package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
@Getter
public class PackagedResImpl extends AbstractRes<PackagedRes> implements PackagedRes {

    public PackagedResImpl(ResourceId<PackagedRes> id, SiteUrl url) {
        super(id, url);
    }
}
