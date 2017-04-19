package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.InputStream;
import java.util.Collections;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class SkipParser implements Parser {
    private final Storage storage;
    private final int priority;

    @SneakyThrows
    public SkipParser(ParserParams<InputStream> params) {
        storage = params.getStorage();
        priority = params.getPriority();
    }

    @Override
    @SneakyThrows
    public ResourceId<ParsedRes> parse(ResourceId<ParsingRes> pingResId) {
        ParsingRes pingRes = storage.getResource(pingResId);
        ResourceId<ParsedRes> parsedResId =
                storage.createParsedRes(pingResId, pingRes.getDownloadedContent(), Collections.emptyList());
        log.debug("Resource is parsed: " + parsedResId);
        return parsedResId;
    }

    @Override
    public boolean accept(String contentType) {
        return true;
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
