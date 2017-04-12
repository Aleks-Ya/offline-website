package ru.yaal.offlinewebsite.api.resource;

/**
 * @author Aleksey Yablokov
 */
public interface Resource<R extends Resource.ResourceId> {
    R getId();

    interface ResourceId {
    }
}
