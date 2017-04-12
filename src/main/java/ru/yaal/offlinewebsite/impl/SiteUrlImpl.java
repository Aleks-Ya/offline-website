package ru.yaal.offlinewebsite.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.SiteUrl;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class SiteUrlImpl implements SiteUrl {
    private final String url;
}
