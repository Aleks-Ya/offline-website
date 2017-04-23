package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.resource.Resource;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Getter
public class ResIdImpl<R extends Resource> implements ResourceId<R> {
    private final String id;
}
