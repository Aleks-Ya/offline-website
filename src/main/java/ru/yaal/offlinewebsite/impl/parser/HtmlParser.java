package ru.yaal.offlinewebsite.impl.parser;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.http.HttpInfo;
import ru.yaal.offlinewebsite.api.params.ParserParams;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.parser.Parser;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.parser.UuidLink;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ParsingRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;
import ru.yaal.offlinewebsite.api.storage.ResourceAlreadyExistsException;
import ru.yaal.offlinewebsite.api.storage.Storage;
import ru.yaal.offlinewebsite.impl.params.SiteUrlImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class HtmlParser implements Parser {
    private final HtmlCleaner cleaner = new HtmlCleaner();
    private final Storage storage;
    private final SiteUrl siteUrl;
    private final List<UrlExtractor<TagNode>> extractors;
    private final int priority;


    @SneakyThrows
    public HtmlParser(ParserParams<TagNode> params) {
        storage = params.getStorage();
        siteUrl = params.getSiteUrl();
        extractors = params.getExtractors();
        priority = params.getPriority();
    }

    @Override
    @SneakyThrows
    public ResourceId<ParsedRes> parse(ResourceId<ParsingRes> pingResId) {
        ParsingRes pingRes = storage.getResource(pingResId);
        InputStream is = pingRes.getDownloadedContent();
        String content = IOUtils.toString(is, Charset.defaultCharset());
        TagNode rootNode = cleaner.clean(content);
        List<UuidLink> allLinks = extractors.stream()
                .map(extractor -> extractor.extract(rootNode, siteUrl))
                .flatMap(Collection::stream)
                .map(link -> {
                    SiteUrl siteUrl = new SiteUrlImpl(link.getAbsolute());
                    try {
                        storage.createNewResource(siteUrl);
                    } catch (ResourceAlreadyExistsException e) {
                        log.debug("Skipped already exists resource: " + siteUrl);
                    }
                    return link;
                })
                .collect(Collectors.toList());
        for (UuidLink link : allLinks) {
            content = content.replaceAll(link.getOriginal(), link.getUUID());
        }
        InputStream resultIs = new ByteArrayInputStream(content.getBytes());
        ResourceId<ParsedRes> pedResId = storage.createParsedRes(pingResId, resultIs, allLinks);
        log.debug("Resource is parsed: " + pedResId);
        return pedResId;
    }

    @Override
    public boolean accept(String contentType) {
        return HttpInfo.ContentTypes.HTML.equals(contentType);
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
