package ru.yaal.offlinewebsite.impl.parser;

import lombok.Getter;
import lombok.NonNull;
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
    @NonNull
    private final String UUID;
    @NonNull
    private final String original;
    @NonNull
    private final String absolute;
}
