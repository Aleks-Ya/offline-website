package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.params.RootPageUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class SameDomainFilter implements Filter<HeadingRes> {
    private final String rootHost;

    @SneakyThrows
    public SameDomainFilter(RootPageUrl rootPageUrl) {
        this.rootHost = new URL(rootPageUrl.getUrl()).getHost();
    }

    @Override
    @SneakyThrows
    public FilterDecision filter(HeadingRes hingRes) {
        boolean accepted = new URL(hingRes.getUrl().getUrl()).getHost().equals(rootHost);
        if (!accepted) {
            return new NegativeDecision("%s: Resource %s rejected", getClass().getSimpleName(), hingRes.getId());
        }
        return PositiveDecision.INSTANCE;
    }
}
