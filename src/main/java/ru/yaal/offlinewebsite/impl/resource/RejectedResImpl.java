package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
public class RejectedResImpl<R extends RejectedRes.Id>
        extends AbstractResource<R>
        implements RejectedRes<R> {

    public RejectedResImpl(R id, SiteUrl url) {
        super(id, url);
    }
}
