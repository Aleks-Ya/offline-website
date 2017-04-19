package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.UuidAbsoluteLink;
import ru.yaal.offlinewebsite.api.resource.PackagingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class PackagingResImpl implements PackagingRes {
    private final ResourceId<PackagingRes> id;
    private final SiteUrl url;
    private final InputStream content;
    private final HttpInfo httpInfo;
    private final List<UuidAbsoluteLink> links;
}