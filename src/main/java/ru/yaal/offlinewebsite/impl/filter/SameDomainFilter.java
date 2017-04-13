package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.NewRes;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class SameDomainFilter implements Filter<NewRes> {
    private final String rootHost;

    @SneakyThrows
    public SameDomainFilter(SiteUrl rootSiteUrl) {
        this.rootHost = new URL(rootSiteUrl.getUrl()).getHost();
    }

    @Override
    @SneakyThrows
    public FilterDecision filter(NewRes newRes) {
        boolean accepted = new URL(newRes.getUrl().getUrl()).getHost().equals(rootHost);
        if (!accepted) {
            return new NegativeDecision("%s: Resource %s rejected", getClass().getSimpleName(), newRes.getId());
        }
        return PositiveDecision.INSTANCE;
    }
}
