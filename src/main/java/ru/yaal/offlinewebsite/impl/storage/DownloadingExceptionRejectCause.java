package ru.yaal.offlinewebsite.impl.storage;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.storage.RejectCause;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class DownloadingExceptionRejectCause implements RejectCause {
    private final String description;

    public DownloadingExceptionRejectCause(Exception e) {
        description = "Exception while downloading resource: " + e.getMessage();
    }
}
