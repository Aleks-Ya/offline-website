package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.params.SiteUrl;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class SameDomainFilter implements Filter<SiteUrl> {
    private final String rootHost;

    @SneakyThrows
    public SameDomainFilter(SiteUrl rootSiteUrl) {
        this.rootHost = new URL(rootSiteUrl.getUrl()).getHost();
    }

    @Override
    @SneakyThrows
    public boolean isAccepted(SiteUrl siteUrl) {
        return new URL(siteUrl.getUrl()).getHost().equals(rootHost);
    }
}
