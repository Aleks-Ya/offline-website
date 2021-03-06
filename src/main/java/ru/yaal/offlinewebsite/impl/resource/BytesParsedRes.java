package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.parser.UuidLink;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.InputStream;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
public class BytesParsedRes implements ParsedRes {
    private final ResourceId<ParsedRes> id;
    private final ResUrl url;
    private final InputStream parsedContent;
    private final HttpInfo httpInfo;
    private final List<UuidLink> links;
}