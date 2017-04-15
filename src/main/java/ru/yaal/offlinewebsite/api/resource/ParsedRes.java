package ru.yaal.offlinewebsite.api.resource;

/**
 * @author Aleksey Yablokov
 */
public interface ParsedRes<C> extends Resource<ParsedRes<C>> {
    C getParsedContent();
}
