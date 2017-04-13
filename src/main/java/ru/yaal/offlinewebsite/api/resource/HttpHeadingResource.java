package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

/**
 * @author Aleksey Yablokov
 */
public interface HttpHeadingResource<R extends HttpHeadingResource.Id> extends Resource<R> {
    R getId();

    SiteUrl getUrl();

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    class Id implements ResourceId {
        private final String id;
    }
}
