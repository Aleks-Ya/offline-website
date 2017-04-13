package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.ParsedResource;
import ru.yaal.offlinewebsite.api.resource.ParsingResource;
import ru.yaal.offlinewebsite.api.storage.Storage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class ParserImpl implements Parser {
    private final HtmlCleaner cleaner = new HtmlCleaner();
    private final SimpleHtmlSerializer serializer = new SimpleHtmlSerializer(cleaner.getProperties());
    private final Storage storage;

    public ParserImpl(ParserParams params) {
        this.storage = params.getStorage();
    }

    @Override
    @SneakyThrows
    public ParsedResource.Id parse(ParsingResource.Id pingResId) {
        ParsingResource<ParsingResource.Id> pingRes = storage.getResource(pingResId);
        InputStream is = pingRes.getDownloadedContent();
        TagNode node = cleaner.clean(is);
        OutputStream os = pingRes.getParsedContentOutputStream();

        ParsedResource.Id pedRes = storage.createParsedRes(pingResId);
        return pedRes;
    }
}
