package ru.yaal.offlinewebsite.api.resource;

import java.util.Comparator;

/**
 * @author Aleksey Yablokov
 */
public interface ResourceComparator extends Comparator<Resource> {
    boolean isFirstGreaterOrEquals(Class<? extends Resource> resClass1, Class<? extends Resource> resClass2);
}
