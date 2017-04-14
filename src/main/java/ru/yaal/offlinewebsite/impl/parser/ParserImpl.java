package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.SimpleHtmlSerializer;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.InputStream;
import java.util.List;

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
    public ResourceId<ParsedRes> parse(ResourceId<ParsingRes> pingResId) {
        ParsingRes<TagNode> pingRes = storage.getResource(pingResId);
        InputStream is = pingRes.getDownloadedContent();
        TagNode rootNode = cleaner.clean(is);
        List<? extends TagNode> hrefs = rootNode.getElementListByName("a", true);
        log.debug("Found {} hrefs on {}", hrefs.size(), pingResId);
        hrefs.stream()
                .map(node -> new SiteUrlImpl(node.getAttributeByName("href")))
                .forEach(storage::createNewResource);
        pingRes.setParsedContent(rootNode);
        ResourceId<ParsedRes> pedResId = storage.createParsedRes(pingResId);
        log.debug("Resource is parsed: " + pedResId);
        return pedResId;
    }
}
