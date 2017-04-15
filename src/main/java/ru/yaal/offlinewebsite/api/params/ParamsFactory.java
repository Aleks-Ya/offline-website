package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.impl.params.DownloaderParamsImpl;

/**
 * @author Aleksey Yablokov
 */
public interface ParamsFactory {
    DownloaderParamsImpl getDownloaderParams();
    DownloadJobParams getJobParams();
}
