package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.resource.ResourceComparator;

/**
 * @author Aleksey Yablokov
 */
public interface StorageParams extends Params {
    ResourceComparator getResourceComparator();
}
