package ru.yaal.offlinewebsite.api.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class NewResource<R extends NewResource.NewResourceId> implements Resource<R> {
    private final R id;
    private final SiteUrl url;

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class NewResourceId implements ResourceId {
        private final String id;
    }
}
