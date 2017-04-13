package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Aleksey Yablokov
 */
public interface RejectedRes<R extends RejectedRes.Id> extends Resource<R> {
    R getId();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    class Id implements ResourceId {
        private final String id;
    }
}
