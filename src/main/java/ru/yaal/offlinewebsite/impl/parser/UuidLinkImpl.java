package ru.yaal.offlinewebsite.impl.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.yaal.offlinewebsite.api.parser.UuidLink;

/**
 * @author Aleksey Yablokov
 */
@RequiredArgsConstructor
@ToString
@Getter
public class UuidLinkImpl implements UuidLink {
    private final String original;
    private final String UUID;
}
