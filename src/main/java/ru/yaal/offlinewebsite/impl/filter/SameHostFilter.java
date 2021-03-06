package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.params.RootResUrl;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.impl.filter.decision.NegativeDecision;
import ru.yaal.offlinewebsite.impl.filter.decision.PositiveDecision;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class SameHostFilter implements HeadingResFilter {
    private static final String FILTER_NAME = SameHostFilter.class.getSimpleName();
    private static final PositiveDecision POSITIVE_DECISION = new PositiveDecision(FILTER_NAME);
    private final String rootHost;
    private final boolean enabled;

    //TODO use SameHostFilterParams
    @SneakyThrows
    public SameHostFilter(RootResUrl rootResUrl, boolean enabled) {
        this.rootHost = new URL(rootResUrl.getUrl()).getHost();
        this.enabled = enabled;
    }

    @Override
    @SneakyThrows
    public FilterDecision filter(HeadingRes hingRes) {
        if (enabled) {
            boolean accepted = new URL(hingRes.getUrl().getUrl()).getHost().equals(rootHost);
            if (!accepted) {
                return new NegativeDecision(FILTER_NAME,
                        "%s: Resource %s rejected", getClass().getSimpleName(), hingRes.getId());
            }
        }
        return POSITIVE_DECISION;
    }
}
