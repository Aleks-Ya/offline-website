package ru.yaal.offlinewebsite.impl.filter;

import ru.yaal.offlinewebsite.api.filter.FilterDecision;

/**
 * @author Aleksey Yablokov
 */
public class NegativeDecision implements FilterDecision {
    private final String message;

    public NegativeDecision(String format, Object... args) {
        this.message = String.format(format, args);
    }

    @Override
    public boolean isAccepted() {
        return false;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
