package ru.yaal.offlinewebsite.api.params;

import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public interface SkipParserParams extends Params {
    Storage getStorage();

    int getPriority();
}
