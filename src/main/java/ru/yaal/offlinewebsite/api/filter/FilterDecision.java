package ru.yaal.offlinewebsite.api.filter;

/**
 * @author Aleksey Yablokov
 */
public interface FilterDecision {
    String getFilterName();

    boolean isAccepted();

    String getMessage();
}
