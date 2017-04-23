package ru.yaal.offlinewebsite.api.filter;

import ru.yaal.offlinewebsite.api.resource.HeadingRes;

/**
 * @author Aleksey Yablokov
 */
public interface HeadingResFilter extends Filter<HeadingRes> {
    @Override
    FilterDecision filter(HeadingRes resource);
}
