package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.storage.RejectCause;

/**
 * TODO add rejection cause
 *
 * @author Aleksey Yablokov
 */
public interface RejectedRes extends Resource<RejectedRes> {
    RejectCause getRejectCause();
}
