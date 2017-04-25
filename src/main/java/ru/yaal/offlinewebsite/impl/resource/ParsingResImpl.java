package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class ParsingResImpl implements ParsingRes {
    private final ResourceId<ParsingRes> id;
    private final ResUrl url;
    private final InputStream downloadedContent;
    private final HttpInfo httpInfo;
}
