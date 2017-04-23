package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.filter.HeadingResFilter;
import ru.yaal.offlinewebsite.api.params.RootLink;
import ru.yaal.offlinewebsite.api.resource.HeadingRes;
import ru.yaal.offlinewebsite.impl.filter.decision.NegativeDecision;
import ru.yaal.offlinewebsite.impl.filter.decision.PositiveDecision;
import ru.yaal.offlinewebsite.impl.parser.UrlHelper;

import java.net.URL;

/**
 * @author Aleksey Yablokov
 */
public class NestedPathFilter implements HeadingResFilter {
    private static final String FILTER_NAME = NestedPathFilter.class.getSimpleName();
    private static final PositiveDecision POSITIVE_DECISION = new PositiveDecision(FILTER_NAME);
    private SameHostFilter sameHostFilter;
    private final String rootPath;
    private final boolean enabled;

    //TODO use NestedPathFilterParams
    @SneakyThrows
    public NestedPathFilter(RootLink rootLink, boolean enabled) {
        sameHostFilter = new SameHostFilter(rootLink, enabled);
        rootPath = UrlHelper.removeLastSegmentFromPath(new URL(rootLink.getOrigin()).getPath());
        this.enabled = enabled;
    }

    @Override
    @SneakyThrows
    public FilterDecision filter(HeadingRes hingRes) {
        if (enabled) {
            FilterDecision sameHostDecision = sameHostFilter.filter(hingRes);
            if (sameHostDecision instanceof NegativeDecision) {
                return sameHostDecision;
            }
            String pagePath = new URL(hingRes.getUrl().getOrigin()).getPath();
            boolean accepted = isStartWith(rootPath, pagePath);
            if (!accepted) {
                return new NegativeDecision(FILTER_NAME, "Resource %s rejected, his path has to start with %s",
                        hingRes.getId(), rootPath);
            }
        }
        return POSITIVE_DECISION;
    }

    private boolean isStartWith(String rootPath, String pagePath) {
        return pagePath.startsWith(rootPath);
    }
}
