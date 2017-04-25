package ru.yaal.offlinewebsite.impl.params;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.RootResUrl;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class ResUrlImpl implements RootResUrl {
    private final String url;
}
