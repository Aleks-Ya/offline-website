package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ResUrl;
import ru.yaal.offlinewebsite.api.resource.DownloadingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class DownloadingResImpl implements DownloadingRes {
    private final ResourceId<DownloadingRes> id;
    private final ResUrl url;
    private final OutputStream outputStream;
    private final HttpInfo httpInfo;
}
