package ru.yaal.offlinewebsite.api.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.SiteUrl;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class NewResource<R extends NewResource.NewResourceId> implements Resource<R> {
    private final R id;
    private final SiteUrl url;

//    public NewResource(NewResourceId id) {
//        this.id = id;
//    }



    @RequiredArgsConstructor
    @Getter
    public static class NewResourceId implements ResourceId {
        private final String id;
    }
}
