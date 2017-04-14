package ru.yaal.offlinewebsite.api.resource;

import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
public interface DownloadingRes extends Resource<DownloadingRes> {
    OutputStream getOutputStream();
}
