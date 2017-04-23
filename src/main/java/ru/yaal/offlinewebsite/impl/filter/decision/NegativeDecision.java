package ru.yaal.offlinewebsite.impl.filter.decision;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class NegativeDecision implements FilterDecision {
    private final String message;
    private final boolean accepted = false;
    private final String filterName;

    public NegativeDecision(String filterName, String format, Object... args) {
        this.message = String.format(format, args);
        this.filterName = filterName;
    }
}
