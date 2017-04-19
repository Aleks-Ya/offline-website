package ru.yaal.offlinewebsite.impl.params;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.RootSiteUrl;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class SiteUrlImpl implements RootSiteUrl {
    private final String url;
}
