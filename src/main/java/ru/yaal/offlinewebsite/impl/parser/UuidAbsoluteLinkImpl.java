package ru.yaal.offlinewebsite.impl.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.parser.UuidAbsoluteLink;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class UuidAbsoluteLinkImpl implements UuidAbsoluteLink {
    private final String original;
    private final String UUID;
    private final String absolute;
}
