package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.PageUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class PackagedResImpl implements PackagedRes {
    private final ResourceId<PackagedRes> id;
    private final PageUrl url;
    private final Path location;
}
