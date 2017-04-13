package ru.yaal.offlinewebsite.impl.filter;

import ru.yaal.offlinewebsite.api.filter.FilterDecision;

/**
 * @author Aleksey Yablokov
 */
public class PositiveDecision implements FilterDecision {
    public static final PositiveDecision INSTANCE = new PositiveDecision();

    private PositiveDecision() {
    }

    @Override
    public boolean isAccepted() {
        return true;
    }

    @Override
    public String getMessage() {
        return "";
    }
}
