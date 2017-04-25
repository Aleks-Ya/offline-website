package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.resource.PackagedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.nio.file.Path;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class PackagedResImpl implements PackagedRes {
    private final ResourceId<PackagedRes> id;
    private final ResUrl url;
    private final Path location;
}
