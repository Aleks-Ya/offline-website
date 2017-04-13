package ru.yaal.offlinewebsite.impl.filter;

import lombok.SneakyThrows;
import ru.yaal.offlinewebsite.api.filter.Filter;
import ru.yaal.offlinewebsite.api.filter.FilterDecision;
import ru.yaal.offlinewebsite.api.resource.HeadedRes;

/**
 * @author Aleksey Yablokov
 */
public class SizeFilter implements Filter<HeadedRes> {
    private final long maxSize;

    @SneakyThrows
    public SizeFilter(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    @SneakyThrows
    public FilterDecision filter(HeadedRes hedRes) {
        boolean accepted = hedRes.getHttpInfo().getContentLength() <= maxSize;
        if (!accepted) {
            return new NegativeDecision("%s: Size exceeded (maxSize=%d, actualSize=%d: $s",
                    getClass().getSimpleName(), maxSize, hedRes.getHttpInfo().getContentLength(), hedRes.getId());
        }
        return PositiveDecision.INSTANCE;
    }
}
