package ru.yaal.offlinewebsite.impl.filter.decision;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class PositiveDecision implements FilterDecision {
    private final String filterName;
    private final boolean accepted = true;
    private final String message = "";

    public PositiveDecision(String filterName) {
        this.filterName = filterName;
    }
}
