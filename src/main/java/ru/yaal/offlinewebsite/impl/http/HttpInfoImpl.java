package ru.yaal.offlinewebsite.impl.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.http.HttpInfo;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@Getter
public class HttpInfoImpl implements HttpInfo {
    private final int responseCode;
    private final long contentLength;
    private final long lastModified;
}
