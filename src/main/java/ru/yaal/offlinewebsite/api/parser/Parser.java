package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface Parser {
    ResourceId<ParsedRes> parse(ResourceId<ParsingRes> parsingResId);

    boolean accept(String contentType);

    int getPriority();
}
