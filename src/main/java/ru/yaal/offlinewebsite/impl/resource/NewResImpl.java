package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.resource.NewRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class NewResImpl implements NewRes {
    private final ResourceId<NewRes> id;
    private final PageUrl url;
}
