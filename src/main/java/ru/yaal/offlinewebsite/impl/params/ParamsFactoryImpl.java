package ru.yaal.offlinewebsite.impl.params;

import ru.yaal.offlinewebsite.api.params.DownloadJobParams;
import ru.yaal.offlinewebsite.api.params.ParamsFactory;

/**
 * @author Aleksey Yablokov
 */
public class ParamsFactoryImpl implements ParamsFactory {
    @Override
    public DownloaderParamsImpl getDownloaderParams() {
        return null;
    }

    @Override
    public DownloadJobParams getJobParams() {
        return null;
    }
}
