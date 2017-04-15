package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class ParserImpl implements Parser<TagNode> {
    private final HtmlCleaner cleaner = new HtmlCleaner();
    private final Storage storage;
    private final URL rootUrl;
    private final List<UrlExtractor<TagNode>> extractors;

    @SneakyThrows
    public ParserImpl(ParserParams<TagNode> params) {
        storage = params.getStorage();
        rootUrl = new URL(params.getRootSiteUrl().getUrl());
        extractors = params.getExtractors();
    }

    @Override
    @SneakyThrows
    public ResourceId<ParsedRes<TagNode>> parse(ResourceId<ParsingRes<TagNode>> pingResId) {
        ParsingRes<TagNode> pingRes = storage.getResource(pingResId);
        InputStream is = pingRes.getDownloadedContent();
        TagNode rootNode = cleaner.clean(is);
        extractors.stream()
                .map(extractor -> extractor.extract(rootNode))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(urlStr -> !urlStr.isEmpty())
                .map(this::newAbsoluteURL)
                .map(URL::toString)
                .map(SiteUrlImpl::new)
                .forEach(siteUrl -> {
                    try {
                        storage.createNewResource(siteUrl);
                    } catch (ResourceAlreadyExistsException e) {
                        log.debug("Skipped already exists resource: " + siteUrl);
                    }
                });
        pingRes.setParsedContent(rootNode);
        ResourceId<ParsedRes<TagNode>> pedResId = storage.createParsedRes(pingResId);
        log.debug("Resource is parsed: " + pedResId);
        return pedResId;
    }

    @SneakyThrows
    private URL newAbsoluteURL(String relativeUrlStr) {
        return new URL(rootUrl, relativeUrlStr);
    }
}
