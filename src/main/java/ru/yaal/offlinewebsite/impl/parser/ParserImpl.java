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
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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
    public ParsedRes.Id parse(ParsingRes.Id pingResId) {
        ParsingRes<ParsingRes.Id> pingRes = storage.getResource(pingResId);
        InputStream is = pingRes.getDownloadedContent();
        TagNode rootNode = cleaner.clean(is);
        List<? extends TagNode> hrefs = rootNode.getElementListByName("a", true);
        log.debug("Found {} hrefs on {}", hrefs.size(), pingResId);
        hrefs.stream()
                .map(node -> new SiteUrlImpl(node.getAttributeByName("href")))
                .forEach(storage::createNewResource);
        OutputStream os = pingRes.getParsedContentOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(os);
//        oos.writeObject(rootNode);
//        oos.close();

        ParsedRes.Id pedResId = storage.createParsedRes(pingResId);
        log.debug("Resource is parsed: " + pedResId);
        return pedResId;
    }
}
