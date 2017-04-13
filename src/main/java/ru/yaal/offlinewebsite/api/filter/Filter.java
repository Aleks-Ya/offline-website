package ru.yaal.offlinewebsite.api.filter;

/**
 * @author Aleksey Yablokov
 */
public interface Filter<R> {
    FilterDecision filter(R resource);
}
