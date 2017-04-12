package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.ThreadPoolParams;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class ThreadPoolParamsImpl implements ThreadPoolParams {
    private final int poolSize;
}
