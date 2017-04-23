package ru.yaal.offlinewebsite.impl.storage;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.storage.RejectCause;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class FilterRejectCause implements RejectCause {
    private final String description;

    public FilterRejectCause(FilterDecision decision) {
        description = decision.toString();
    }
}
