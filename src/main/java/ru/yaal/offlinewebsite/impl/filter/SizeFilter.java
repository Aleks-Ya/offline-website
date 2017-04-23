package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.filter.HeadedResFilter;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;
import ru.yaal.offlinewebsite.impl.filter.decision.NegativeDecision;
import ru.yaal.offlinewebsite.impl.filter.decision.PositiveDecision;

/**
 * TODO use separated class for Filter name
 *
 * @author Aleksey Yablokov
 */
public class SizeFilter implements HeadedResFilter {
    private static final String FILTER_NAME = SizeFilter.class.getSimpleName();
    private static final PositiveDecision POSITIVE_DECISION = new PositiveDecision(FILTER_NAME);
    private final long maxSizeBytes;
    private final boolean enabled;

    @SneakyThrows
    public SizeFilter(long maxSizeBytes, boolean enabled) {
        this.maxSizeBytes = maxSizeBytes;
        this.enabled = enabled;
    }

    @Override
    @SneakyThrows
    public FilterDecision filter(HeadedRes hedRes) {
        if (enabled) {
            boolean accepted = hedRes.getHttpInfo().getContentLength() <= maxSizeBytes;
            if (!accepted) {
                return new NegativeDecision(FILTER_NAME, "%s: Size exceeded (maxSizeBytes=%d, actualSize=%d: $s",
                        getClass().getSimpleName(), maxSizeBytes, hedRes.getHttpInfo().getContentLength(), hedRes.getId());
            }
        }
        return POSITIVE_DECISION;
    }
}
