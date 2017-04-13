package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;

/**
 * @author Aleksey Yablokov
 */
public interface Parser {
    ParsedRes.Id parse(ParsingRes.Id pingResId);

}
