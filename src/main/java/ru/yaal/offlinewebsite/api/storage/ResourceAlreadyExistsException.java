package ru.yaal.offlinewebsite.api.storage;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.resource.Resource;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
public class ResourceAlreadyExistsException extends RuntimeException {
    private final Resource.ResourceId resourceId;
}
