package ru.yaal.offlinewebsite.api.filter;

import ru.yaal.offlinewebsite.api.resource.HeadedRes;

/**
 * @author Aleksey Yablokov
 */
public interface HeadedResFilter extends Filter<HeadedRes> {
    @Override
    FilterDecision filter(HeadedRes resource);
}
