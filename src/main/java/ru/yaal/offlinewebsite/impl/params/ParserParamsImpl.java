package ru.yaal.offlinewebsite.impl.params;

import lombok.Getter;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
@ToString
@Getter
public class ParserParamsImpl implements ParserParams {
    private final Storage storage;
    private final SiteUrl rootSiteUrl;

    public ParserParamsImpl(Storage storage, SiteUrl rootSiteUrl) {
        this.storage = storage;
        this.rootSiteUrl = rootSiteUrl;
    }
}
