package ru.yaal.offlinewebsite.impl.packager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.packager.ReplacerParams;
import ru.yaal.offlinewebsite.api.params.RootSiteUrl;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.storage.Storage;

/**
 * @author Aleksey Yablokov
 */
@ToString
@RequiredArgsConstructor
@Getter
public class ReplacerParamsImpl implements ReplacerParams {
    private final Storage storage;
    private final RootSiteUrl rootSiteUrl;
}
