package ru.yaal.offlinewebsite.impl.params;

import ru.yaal.offlinewebsite.api.SiteUrl;
import ru.yaal.offlinewebsite.api.ioc.Factory;
import ru.yaal.offlinewebsite.api.params.JobParams;

/**
 * @author Aleksey Yablokov
 */
public class JobParamsImpl implements JobParams {
    private SiteUrl url;

    public JobParamsImpl(SiteUrl url) {
        this.url = url;
    }

    @Override
    public SiteUrl getSiteUrl() {
        return url;
    }

    @Override
    public Factory getFactory() {
        return null;
    }
}
