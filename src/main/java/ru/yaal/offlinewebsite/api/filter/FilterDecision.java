package ru.yaal.offlinewebsite.api.filter;

/**
 * @author Aleksey Yablokov
 */
public interface FilterDecision {
    boolean isAccepted();

    String getMessage();
}
