package ru.yaal.offlinewebsite.impl.link;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.link.ResLink;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@Getter
@RequiredArgsConstructor
public class ResLinkImpl<R> implements ResLink<R> {
    private final ResourceId<R> resourceId;
    private final String origin;
}
