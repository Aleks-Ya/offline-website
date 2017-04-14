package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.NewRes;

/**
 * @author Aleksey Yablokov
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class NewResImpl<R extends NewRes.Id>
        extends AbstractResource<R>
        implements NewRes<R> {
    public NewResImpl(R id, SiteUrl url) {
        super(id, url);
    }
}
