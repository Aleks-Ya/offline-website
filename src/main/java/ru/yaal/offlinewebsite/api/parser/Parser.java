package ru.yaal.offlinewebsite.api.parser;

import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
public interface Parser<C> {
    ResourceId<ParsedRes<C>> parse(ResourceId<ParsingRes<C>> pingResId);

}
