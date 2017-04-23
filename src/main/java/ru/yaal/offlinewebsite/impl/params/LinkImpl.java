package ru.yaal.offlinewebsite.impl.params;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.params.RootLink;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Getter
public class LinkImpl implements RootLink {
    private final String origin;
}
