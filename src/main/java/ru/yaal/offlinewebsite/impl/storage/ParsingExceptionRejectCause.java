package ru.yaal.offlinewebsite.impl.storage;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.storage.RejectCause;

/**
 * @author Aleksey Yablokov
 */
@Getter
@ToString
public class ParsingExceptionRejectCause implements RejectCause {
    private final String description;

    public ParsingExceptionRejectCause(Exception e) {
        description = "Exception while parsing resource: " + e.getMessage();
    }
}
