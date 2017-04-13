package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.resource.ParsedResource;
import ru.yaal.offlinewebsite.api.resource.ParsingResource;

/**
 * @author Aleksey Yablokov
 */
public interface Parser {
    ParsedResource.Id parse(ParsingResource.Id pingResId);

}
