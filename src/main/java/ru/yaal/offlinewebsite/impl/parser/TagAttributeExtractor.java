package ru.yaal.offlinewebsite.impl.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.Link;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;
import ru.yaal.offlinewebsite.api.parser.UuidLink;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * TODO what about #fragments?
 *
 * @author Aleksey Yablokov
 */
@Slf4j
public class TagAttributeExtractor implements UrlExtractor<TagNode> {
    private final String tag;
    private final String attribute;

    public TagAttributeExtractor(Params params) {
        tag = params.getTag();
        attribute = params.getAttribute();
    }

    @Override
    public List<UuidLink> extract(TagNode content, Link link) {
        return content.getElementListByName(tag, true).stream()
                .map(node -> node.getAttributeByName(attribute))
                .filter(href -> href != null && !href.isEmpty())
                .map(href -> new UuidLinkImpl(UUID.randomUUID().toString(), href,
                        UrlHelper.toAbsoluteUrlStr(link.getOrigin(), href)))
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    @Getter
    public static class Params implements ru.yaal.offlinewebsite.api.params.Params {
        private final String tag;
        private final String attribute;
    }
}
