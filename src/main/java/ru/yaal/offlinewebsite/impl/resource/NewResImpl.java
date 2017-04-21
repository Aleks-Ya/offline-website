package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class NewResImpl extends AbstractRes<NewRes> implements NewRes {

    public NewResImpl(ResourceId<NewRes> id, PageUrl url) {
        super(id, url);
    }
}
