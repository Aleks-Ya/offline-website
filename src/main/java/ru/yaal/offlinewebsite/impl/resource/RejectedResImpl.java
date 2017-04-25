package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.resource.RejectedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.RejectCause;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class RejectedResImpl implements RejectedRes {
    private final ResourceId<RejectedRes> id;
    private final ResUrl url;
    private final RejectCause rejectCause;
}
