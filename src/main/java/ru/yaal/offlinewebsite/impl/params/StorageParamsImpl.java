package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.StorageParams;
import ru.yaal.offlinewebsite.api.resource.ResourceComparator;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class StorageParamsImpl implements StorageParams {
    private final ResourceComparator resourceComparator;

    public StorageParamsImpl(ResourceComparator resourceComparator) {
        this.resourceComparator = resourceComparator;
    }
}
