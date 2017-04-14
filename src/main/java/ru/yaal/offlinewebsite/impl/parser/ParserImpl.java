package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class ParserImpl implements Parser {
    private final HtmlCleaner cleaner = new HtmlCleaner();
    private final Storage storage;
    private final URL rootUrl;

    @SneakyThrows
    public ParserImpl(ParserParams params) {
        storage = params.getStorage();
        rootUrl = new URL(params.getRootSiteUrl().getUrl());
//        assert rootUrl.toString().endsWith("/");
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
                .map(node -> node.getAttributeByName("href"))
                .map(this::newAbsoluteURL)
                .map(URL::toString)
                .map(SiteUrlImpl::new)
                .forEach(storage::createNewResource);
        pingRes.setParsedContent(rootNode);
        ResourceId<ParsedRes> pedResId = storage.createParsedRes(pingResId);
        log.debug("Resource is parsed: " + pedResId);
        return pedResId;
    }

    @SneakyThrows
    private URL newAbsoluteURL(String relativeUrlStr) {
        return new URL(rootUrl, relativeUrlStr);
    }
}
