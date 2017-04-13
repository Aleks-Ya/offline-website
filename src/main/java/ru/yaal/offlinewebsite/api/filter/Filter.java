package ru.yaal.offlinewebsite.api.filter;

/**
 * @author Aleksey Yablokov
 */
public interface Filter<R> {
    boolean isAccepted(R resource);
}
