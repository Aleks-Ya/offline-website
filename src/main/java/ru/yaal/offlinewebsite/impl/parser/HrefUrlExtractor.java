package ru.yaal.offlinewebsite.impl.parser;

import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.parser.UrlExtractor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aleksey Yablokov
 */
@Slf4j
public class HrefUrlExtractor implements UrlExtractor<TagNode> {
    @Override
    public List<String> extract(TagNode content) {
        return content.getElementListByName("a", true).stream()
                .map(node -> node.getAttributeByName("href"))
                .collect(Collectors.toList());
    }
}