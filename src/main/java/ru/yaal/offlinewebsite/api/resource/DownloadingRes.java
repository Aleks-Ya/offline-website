package ru.yaal.offlinewebsite.api.resource;

import ru.yaal.offlinewebsite.api.http.HttpInfo;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadingRes extends Resource<DownloadingRes> {
    OutputStream getOutputStream();
    HttpInfo getHttpInfo();
}
