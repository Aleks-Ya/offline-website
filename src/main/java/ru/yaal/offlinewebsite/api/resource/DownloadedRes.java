package ru.yaal.offlinewebsite.api.resource;

import java.io.InputStream;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadedRes extends Resource<DownloadedRes> {
    InputStream getContent();
}
