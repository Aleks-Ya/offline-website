package ru.yaal.offlinewebsite.impl.params;

import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
public class ParserParamsImpl implements ParserParams {
    private final Storage storage;

    public ParserParamsImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }
}
