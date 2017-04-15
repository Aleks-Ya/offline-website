package ru.yaal.offlinewebsite.api.resource;

/**
 * @author Aleksey Yablokov
 */
public interface PackagingRes<C> extends Resource<PackagingRes> {
    C getContent();
}
