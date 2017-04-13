package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yaal.offlinewebsite.api.params.HeadRequestParams;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.api.system.Network;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@Getter
public class HeadRequestParamsImpl implements HeadRequestParams {
    private final Storage storage;
    private final Network network;
}
