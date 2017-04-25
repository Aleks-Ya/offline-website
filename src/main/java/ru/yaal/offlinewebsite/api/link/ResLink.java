package ru.yaal.offlinewebsite.api.link;

import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface ResLink<R> extends Link {
    ResourceId<R> getResourceId();
}
