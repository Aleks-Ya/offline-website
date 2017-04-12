package ru.yaal.offlinewebsite.api.resource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class NewResource<R extends NewResource.NewResourceId> implements Resource<R> {
    private final R id;

//    public NewResource(NewResourceId id) {
//        this.id = id;
//    }



    @RequiredArgsConstructor
    @Getter
    public static class NewResourceId implements ResourceId {
        private final String id;
    }
}
