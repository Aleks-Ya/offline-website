package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;

/**
 * @author Aleksey Yablokov
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class HeadingResImpl<R extends HeadingRes.Id>
        extends AbstractResource<R>
        implements HeadingRes<R> {
    public HeadingResImpl(R id, SiteUrl url) {
        super(id, url);
    }
}
