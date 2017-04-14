package ru.yaal.offlinewebsite.impl.resource;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.htmlcleaner.TagNode;
import ru.yaal.offlinewebsite.api.params.SiteUrl;
import ru.yaal.offlinewebsite.api.resource.ParsedRes;
import ru.yaal.offlinewebsite.api.resource.ResourceId;

/**
 * @author Aleksey Yablokov
 */
@EqualsAndHashCode(callSuper = true)
public class BytesParsedRes extends AbstractRes<ParsedRes> implements ParsedRes<TagNode> {

    @Getter
    private final TagNode parsedContent;

    public BytesParsedRes(ResourceId<ParsedRes> id, SiteUrl url, TagNode parsedContent) {
        super(id, url);
        this.parsedContent = parsedContent;
    }
}
